package com.umc.ttg.global.util.uuid;

import org.springframework.stereotype.Component;

//@Component
public class TestUuidHolder implements UuidHolder {
    @Override
    public String randomUuid() {
        return "aaaa-aaaa-aaaa";
    }
}
