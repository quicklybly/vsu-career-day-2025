# Решения задач

## Concurrency

### Counter

Классическая задача на счетчик, решается через atomic переменную.

```java
public class Counter {
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        var executor = Executors.newFixedThreadPool(100);
        int targetCount = 100_000;

        for (int i = 0; i < targetCount; i++) {
            executor.submit(() -> count.incrementAndGet());
        }

        executor.shutdown();
        boolean result = executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(count.get());
        System.exit(result ? 0 : 1);
    }
}
```

### SimpleStack

Задача - реализовать неблокирующий стек, не использую коллекции из пакета java.concurrent.
Решение - [стек Трайбера](https://en.wikipedia.org/wiki/Treiber_stack)

```java
public class SimpleStackImpl implements SimpleStack {
    private static class Node {
        int value;
        Node prev;

        Node(int value, Node prev) {
            this.value = value;
            this.prev = prev;
        }

        Node(int value) {
            this(value, null);
        }
    }

    private final AtomicReference<Node> head = new AtomicReference<>(null);

    @Override
    public void push(Integer value) {
        Node newHead = new Node(value);
        Node prevHead;

        do {
            prevHead = head.get();
            newHead.prev = prevHead;
        } while (!head.compareAndSet(prevHead, newHead));
    }

    @Override
    public Integer pull() {
        Node currHead;
        Node newHead;
        do {
            currHead = head.get();
            if (currHead == null) {
                return null;
            }
            newHead = currHead.prev;
        } while (!head.compareAndSet(currHead, newHead));
        return currHead.value;
    }
}
```

## Streams

### External Call

Ожидание ответа от внешнего сервиса приводит к тому, что
время выполнения всех операций в цепочке будет равно сумме времени ответа внешних сервисов, это
можно обойти с помощью маппинга стрима в CompletableFuture, тогда время выполнения будет равно времени ожидания
самой долгой операции.

```java
public class ExternalCall {

    public static void main(String[] args) {
        var client = new ExternalCallClient();

        List<CompletableFuture<Integer>> futures = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map(client::call)
                .toList();

        CompletableFuture<List<Integer>> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(_ -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());

        // total time is 3 seconds
        allFutures.thenAccept(results -> results.stream()
                        .filter(p -> p % 2 != 0)
                        .limit(3)
                        .forEach(System.out::println))
                .join();
    }
}
```

### Stream State

Из трех методов операций sorted() будет выполнена только последняя, это связано с внутренним устройством Stream API.
Сплитератор для стрима хранит набор флагов, один из которых - флаг сортировки.

При создании стрима из TreeSet, флаг сортировки установлен в true.

```java
var c = data.stream()
        .sorted() // не будет выполнено, потому что TreeSet уже отсортирован
        .filter(i -> i % 2 != 0) // не влияет на флаг сортировки
        .sorted() // не будет выполнено, что значение флага все еще true
        .map(i -> i * 2) // устанавливает флаг сортировки в false
        .sorted() // будет выполнена сортировка
        .toList();
```

### Tell Me Output

Не будет выведено ни одного элемента, потому что нет терминальной операции.

## Spring

Задача - провести код-ревью кода.
Проблемы:

* Имена переменных KT и ONPAGE не выразительны
* Неверный порядок модификаторов для переменных KT и ONPAGE
* Аннотацию @Bean не ставиться над классом, должна быть @Component или @Service
* Нет конструктора
* Поля repo и kafka должны быть финальными
* Используется общее состояние в виде id и title, что приведет к неопределенному результату выполнения в многопоточной
  среде
* Для записи в два независимых источника данных (БД и Kafka) следует использовать transactional outbox
* Пагинация выполняется на стороне сервера, а не БД
* Логирование через System.out.println, а не фреймворки логирования
* И еще очень много проблем

```java

@Bean
public class ContactService {
    static public String KT = "TOPIC";
    static public int ONPAGE = 10;

    public ContactRepository repo;
    public KafkaTemplate<String, String> kafka;

    private long id = 0;
    private String title;

    void save(Long a, String b) {
        id = a;
        title = b;
        var contact = new Contact(id, title);
        repo.save(contact);
        kafka.send(KT, id + " " + title);
    }

    public List<Contact> getPage(int page) {
        List<Contact> contacts = (List<Contact>) repo.findAll();
        var res = new ArrayList<Contact>();
        for (int i = 0; i < ONPAGE; i++) {
            Contact c = contacts.get(i + page * ONPAGE);
            res.add(c);
        }
        System.out.println("retrieved " + res.size() + " contacts" + " from page " + page);
        return res;
    }
}
```
