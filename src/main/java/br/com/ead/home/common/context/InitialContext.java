package br.com.ead.home.common.context;

public interface InitialContext {

    void bind(String key, Object value);

    Object lookup(String key);

}
