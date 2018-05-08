package com.example.miniproject.moneybook;

public class MoneyHistory {
    int amount;
    String content;
    int count;
    String statement;

    MoneyHistory(int amount,String content, int count, String statement){
        this.amount = amount;
        this.content = content;
        this.count = count;
        this.statement = statement;
    }

    public int getAmount(){
        return amount;
    }

    public String getContent(){
        return content;
    }

    public  int getCount(){
        return count;
    }

    public String getStatement(){
        return statement;
    }

    public void setAmount(int data1){
        this.amount = amount;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void  setCount(int count){
        this.count = count;
    }

    public void  setStatement(String statement){
        this.statement = statement;
    }

    @Override
    public String toString() {
        return amount + " | " +content+ " | " + count+ " | " + statement ;
    }
}
