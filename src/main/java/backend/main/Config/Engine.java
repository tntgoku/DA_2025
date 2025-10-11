package backend.main.Config;

import java.util.UUID;

public class Engine {
    public static String baseUrl = "http://localhost:8080";

    public static Integer convertString(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String makeSlug(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }

        String slug = name.trim().toLowerCase();
        slug = slug.replaceAll("[^a-z0-9\\s]", "");
        slug = slug.replaceAll("\\s+", "-");
        slug = slug.replaceAll("^-+|-+$", "");
        return slug;
    }

    public static String generateShortName(String name) {
        if (name == null || name.isEmpty())
            return "";

        // Chuẩn hóa: bỏ khoảng trắng, chuyển chữ thường thành chữ thường có dấu
        name = name.trim();

        // Tách các từ (vd: "Phụ kiện sửa chữa" → ["Phụ", "kiện", "sửa", "chữa"])
        String[] parts = name.split("\\s+");

        StringBuilder shortName = new StringBuilder();

        // Nếu có số trong tên (ví dụ "Iphone 15"), lấy 2 ký tự đầu của từ đầu và phần
        // có số
        boolean hasNumber = name.matches(".*\\d+.*");
        if (hasNumber && parts.length >= 2) {
            String prefix = parts[0].substring(0, 2).toUpperCase();
            String numberPart = "";
            for (String p : parts) {
                if (p.matches(".*\\d+.*")) {
                    numberPart = p.replaceAll("\\D+", ""); // chỉ lấy số
                    break;
                }
            }
            return prefix + numberPart;
        }

        // Nếu không có số → lấy ký tự đầu của từng từ (tối đa 4 từ)
        for (int i = 0; i < parts.length && i < 4; i++) {
            shortName.append(removeAccent(parts[i].substring(0, 1)).toUpperCase());
        }

        return shortName.toString();
    }

    // Hàm loại bỏ dấu tiếng Việt để sinh mã chuẩn
    private static String removeAccent(String s) {
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        return temp.replaceAll("\\p{M}", "");
    }

    public static String makeSKU(String name) {
        return "SKU-" + name + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
