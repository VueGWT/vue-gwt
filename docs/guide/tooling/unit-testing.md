# Unit Testing

Components written using Vue GWT can be unit tested using Java testing frameworks like `JUnit`.
You can just instantiate an instance of your Java Component in your unit test with `new`.

You can then call Vue hooks like `created` in your test to initialize your Component.

There are a few limitations though:

* If your code calls methods of `JsObject` or `JsArray`, it won't work in Java.
* Parts of your code that call native methods like `$watch` or `$emit` won't work either.

You might want to separate the calls to these methods/object in methods that shouldn't be tested.

Example of a Unit test for a Pagination Component:

```java
public class PaginationComponentTest {
    PaginationComponent paginationComponent;

    @Before
    public void setUp() throws Exception {
        paginationComponent = new PaginationComponent();
        paginationComponent.created();
    }

    @Test
    public void getPageNumbers_singlePage() {
        paginationComponent.page = createPage(0, 1, 10);
        assertEquals(Collections.singletonList(0), paginationComponent.getPageNumbers());
    }
    
    @Test
    public void getPageNumbers_twoPages() {
        paginationComponent.page = createPage(1, 2, 10);
        assertEquals(Arrays.asList(0, 1), paginationComponent.getPageNumbers());
    }

    @Test
    public void getPageNumbers_fourPages() {
        paginationComponent.page = createPage(3, 4, 10);
        assertEquals(Arrays.asList(0, 1, 2, 3), paginationComponent.getPageNumbers());
    }
    
    // ...
    
    private Page createPage(int number, int totalPages, int size) {
        return getPage(number, totalPages, size, totalPages * size);
    }

    private Page createPage(int number, int totalPages, int size, int totalElements) {
        Page result = new Page();
        result.number = number;
        result.totalPages = totalPages;
        result.size = size;
        result.totalElements = totalElements;

        return result;
    }
}
```