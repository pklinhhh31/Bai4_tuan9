package com.bank.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;

public class FilePathTest {
    @Test
    void testSeparatorDifference() {
        // Ta kỳ vọng dấu phân cách thư mục của hệ thống phải là dấu \
        // File.separator sẽ trả về \ trên Windows và / trên Linux/macOS
        String expectedSeparator = "\\";

        assertEquals(expectedSeparator, File.separator,
                "Lỗi: Hệ điều hành này không sử dụng dấu gạch chéo ngược!");
    }
}