package com.bank.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp quản lý danh sách khách hàng và các nghiệp vụ ngân hàng.
 */
public class Bank {

    private static final Logger logger = LoggerFactory.getLogger(Bank.class);

    // Regex pattern để tránh Magic String
    private static final String ID_PATTERN = "\\d{9}";

    private List<Customer> customers;

    public Bank() {
        this.customers = new ArrayList<>();
    }

    public List<Customer> getCustomerList() {
        return customers;
    }

    /**
     * Cập nhật danh sách khách hàng.
     *
     * @param customerList danh sách khách hàng mới.
     */
    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customers = new ArrayList<>();
        } else {
            this.customers = customerList;
        }
    }

    /**
     * Đọc dữ liệu khách hàng từ InputStream.
     * Đã Refactor để giảm lồng ghép (nested if) và sử dụng Logger.
     */
    public void readCustomerList(InputStream inputStream) {
        logger.info("Bắt đầu đọc dữ liệu khách hàng từ stream.");

        if (inputStream == null) {
            logger.warn("InputStream bị null, bỏ qua việc đọc dữ liệu.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Customer currentCustomer = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                int lastSpaceIndex = line.lastIndexOf(' ');
                if (lastSpaceIndex <= 0) {
                    continue;
                }

                String lastToken = line.substring(lastSpaceIndex + 1).trim();

                if (lastToken.matches(ID_PATTERN)) {
                    // Trường hợp dòng là thông tin Khách hàng (Tên + ID)
                    String name = line.substring(0, lastSpaceIndex).trim();
                    currentCustomer = new Customer(Long.parseLong(lastToken), name);
                    customers.add(currentCustomer);
                    logger.debug("Đã thêm khách hàng: {} - ID: {}", name, lastToken);

                } else if (currentCustomer != null) {
                    // Trường hợp dòng là thông tin Tài khoản (Số TK + Loại + Số dư)
                    processAccountLine(line, currentCustomer);
                }
            }
        } catch (IOException | NumberFormatException e) {
            logger.error("Lỗi xảy ra khi đọc hoặc phân tích dữ liệu khách hàng: {}", e.getMessage());
        }
    }

    /**
     * Hàm hỗ trợ tách logic xử lý tài khoản để code gọn hơn.
     */
    private void processAccountLine(String line, Customer customer) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            return;
        }

        try {
            long accNum = Long.parseLong(parts[0]);
            String type = parts[1];
            double balance = Double.parseDouble(parts[2]);

            if (Account.CHECKING_TYPE.equals(type)) {
                customer.addAccount(new CheckingAccount(accNum, balance));
            } else if (Account.SAVINGS_TYPE.equals(type)) {
                customer.addAccount(new SavingsAccount(accNum, balance));
            }
        } catch (NumberFormatException e) {
            logger.warn("Dòng dữ liệu tài khoản không hợp lệ: {}", line);
        }
    }

    /**
     * Lấy thông tin khách hàng sắp xếp theo ID.
     */
    public String getCustomersInfoByIdOrder() {
        // Sử dụng Lambda để thay thế Anonymous Class
        customers.sort(Comparator.comparingLong(Customer::getIdNumber));
        return formatCustomerInfo(customers);
    }

    /**
     * Lấy thông tin khách hàng sắp xếp theo Tên, nếu trùng tên thì xếp theo ID.
     */
    public String getCustomersInfoByNameOrder() {
        List<Customer> sortedList = new ArrayList<>(customers);
        sortedList.sort(Comparator.comparing(Customer::getFullName)
                .thenComparingLong(Customer::getIdNumber));

        return formatCustomerInfo(sortedList);
    }

    /**
     * Hàm dùng chung để định dạng danh sách khách hàng thành chuỗi, sử dụng StringBuilder.
     */
    private String formatCustomerInfo(List<Customer> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getCustomerInfo());
            if (i < list.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}