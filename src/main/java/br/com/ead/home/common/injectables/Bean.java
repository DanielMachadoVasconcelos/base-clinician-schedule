package br.com.ead.home.common.injectables;

public interface Bean {

    default String beanName(){
        return this.getClass().getName();
    }
}
