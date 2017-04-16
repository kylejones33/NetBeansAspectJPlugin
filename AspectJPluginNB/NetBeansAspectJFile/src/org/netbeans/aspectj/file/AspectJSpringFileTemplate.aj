public aspect myaspect {

    before(): execution(* *.test()) {
        System.out.println("This is before my aspect");
    }

    after(): execution(* *.test()) {
        System.out.println("This is after my aspect");
    }

//test using pointcuts
    pointcut callPointCutTest(): execution(* *.test());
    before(): callPointCutTest()
    {     System.out.println("This is before my aspect");  }
    
    after(): callPointCutTest() 
    {     System.out.println("This is after my aspect");   }

}