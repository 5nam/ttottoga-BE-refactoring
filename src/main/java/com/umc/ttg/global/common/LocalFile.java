package com.umc.ttg.global.common;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalFile {
    private String key;
    private String path;
}
