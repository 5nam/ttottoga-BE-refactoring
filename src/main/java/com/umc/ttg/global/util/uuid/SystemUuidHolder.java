package com.umc.ttg.global.util.uuid;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {
    @Override
    public String randomUuid() {
        return UUID.randomUUID().toString();
    }
}
