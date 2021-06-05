package com.bradenn.dev.enclave.models;

import org.bson.conversions.Bson;

public interface Model {
    Bson getQuery();

}
