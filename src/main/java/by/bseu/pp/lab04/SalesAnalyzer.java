package by.bseu.pp.lab04;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class SalesAnalyzer {
    private SalesAnalyzer() { }

    /** Продажи за период включительно. Порядок исходного списка сохраняется. Используйте Stream API. */
    public static List<Sale> filterByPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return sales.stream()
                .filter(s -> !s.getDate().isBefore(from) && !s.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    /** Все продажи с точным совпадением кода товара. Сохраняйте исходный порядок. */
    public static List<Sale> findByProductCode(List<Sale> sales, String productCode) {
        validateSales(sales);
        if (productCode == null || productCode.isBlank()) {
            throw new IllegalArgumentException("Код товара должен быть непустым");
        }
        return sales.stream()
                .filter(s -> s.getProductCode().equals(productCode))
                .collect(Collectors.toList());
    }

    /** Новый список по убыванию суммы продажи; при равенстве суммы — по id по возрастанию. */
    public static List<Sale> sortByAmountDescending(List<Sale> sales) {
        validateSales(sales);
        List<Sale> result = new ArrayList<>(sales);
        result.sort(Comparator.comparingDouble(Sale::getAmount).reversed()
                .thenComparing(Sale::getId));
        return result;
    }

    /** Коды покупателей без повторений. Порядок элементов не задан. */
    public static Set<String> uniqueCustomerCodes(List<Sale> sales) {
        validateSales(sales);
        return sales.stream()
                .map(Sale::getCustomerCode)
                .collect(Collectors.toSet());
    }

    /** Категория → список продаж. Внутри группы сохраняйте исходный порядок. Используйте Stream API. */
    public static Map<String, List<Sale>> groupByCategory(List<Sale> sales) {
        validateSales(sales);
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getCategory));
    }

    /** Сумма продаж за период включительно, для пустого результата — 0. Используйте Stream API. */
    public static double totalAmountForPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return sales.stream()
                .filter(s -> !s.getDate().isBefore(from) && !s.getDate().isAfter(to))
                .mapToDouble(Sale::getAmount)
                .sum();
    }

    /** Сумма продаж каждой категории за период. Используйте Stream API. */
    public static Map<String, Double> amountByCategoryForPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return sales.stream()
                .filter(s -> !s.getDate().isBefore(from) && !s.getDate().isAfter(to))
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.summingDouble(Sale::getAmount)));
    }

    /** Сумма продаж по датам за период. Используйте Stream API. */
    public static Map<LocalDate, Double> amountByDateForPeriod(List<Sale> sales, LocalDate from, LocalDate to) {
        validateSales(sales);
        validatePeriod(from, to);
        return sales.stream()
                .filter(s -> !s.getDate().isBefore(from) && !s.getDate().isAfter(to))
                .collect(Collectors.groupingBy(
                        Sale::getDate,
                        Collectors.summingDouble(Sale::getAmount)));
    }

    // Проверки уже готовы. Изменять их не требуется.
    private static void validateSales(List<Sale> sales) {
        if (sales == null) {
            throw new IllegalArgumentException("Список продаж не задан");
        }
        for (Sale sale : sales) {
            if (sale == null) {
                throw new IllegalArgumentException("В списке есть null");
            }
        }
    }

    private static void validatePeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException("Некорректный период");
        }
    }
}
