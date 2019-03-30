package com.bobomico.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: com.quartz.stevexie.observer
 * @Author: Lion
 * @Date: 2019/3/24  0:15
 * @Description: 目标对象 它知道观察它的观察者 并提供注册和删除观察者的接口
 * @version:
 */
public class Subject {
    // 订阅者列表
    private List<Observer> observers = new ArrayList();

    // 注册订阅者
    public void attach(Observer observer){
        observers.add(observer);
    }

    // 删除订阅者
    public void detach(Observer observer){
        observers.remove(observer);
    }

    // 通知
    protected void notifyObservers(){
        for(Observer observer : observers){
            observer.update(this);
        }
    }
}
