package com.bank.system;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilePathTest {
    @Test
    void testPortablePath() {
        // Cách 1: Dùng File.separator (Tự động đổi thành / hoặc \ tùy OS)
        String portablePath = "data" + File.separator + "customers.txt";

        // Cách 2: Dùng Path API (Hiện đại và khuyến khích dùng cho Java 25)
        Path path = Paths.get("data", "customers.txt");

        assertNotNull(path.toString());
        System.out.println("Đường dẫn chuẩn trên OS này là: " + path.toString());
    }
}