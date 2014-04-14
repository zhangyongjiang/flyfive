package com.flyerfive.client.data;

import java.util.List;

public abstract class SelectProperty<T> extends PrimeProperty<T> {
    abstract public List<String> getOptions();
}
