package hello;


public class Hello {

    public void print() {
        this.print();
        System.out.println(this);
        System.out.println("hello");
    }

    public void call() {
        print();

    }
}
