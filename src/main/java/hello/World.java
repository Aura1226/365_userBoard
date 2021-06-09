package hello;

import lombok.extern.log4j.Log4j2;

@Log4j2
class World extends Hello {


    public void call() {
        super.print();
    }
    public void print() {
        System.out.println("sdasd");
    }

}
