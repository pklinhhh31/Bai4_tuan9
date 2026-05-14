package com.bank.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;

public class FilePathTest {
    @Test
    void testHardcodedPath() {
        // Cố tình dùng dấu \ của Windows
        String hardcodedPath = "data\\customers.txt"; 
        File file = new File(hardcodedPath);
        
        // Test này sẽ PASS trên Windows nhưng FAIL trên Linux/macOS
        // vì Linux coi dấu \ là một phần của tên file chứ không phải phân tách thư mục
        assertTrue(file.getPath().contains("\\"), "Path should use Windows separator");
    }
}