import java.util.*;

public class ClockPageReplacement {
    static class Page {
        int number;
        boolean referenced;

        Page(int number) {
            this.number = number;
            this.referenced = false;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Nhập số lượng khung trang (frames) và chuỗi trang cần truy cập
        System.out.print("Nhập số lượng khung trang: ");
        int framesCount = scanner.nextInt();

        System.out.print("Nhập chuỗi trang (dùng dấu cách để phân cách): ");
        scanner.nextLine(); // Bỏ qua dòng mới sau khi nhập số nguyên
        String[] pageInput = scanner.nextLine().split(" ");
        int pageNumber = pageInput.length;
        Integer[] pages = new Integer[pageNumber];
        for (int i = 0; i < pageNumber; i++) {
            pages[i] = Integer.parseInt(pageInput[i]);
        }

        // Tạo mảng khung trang
        String[][] displayFrames = new String[framesCount][pageNumber];
        String[][] displayStatus = new String[framesCount][pageNumber];
        List<String> currentFrames = new ArrayList<>();
        List<String> currentStatus = new ArrayList<>();
        boolean[] faults = new boolean[pageNumber];
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        int pointer = 0; // Con trỏ cho thuật toán CLOCK
        for(int i = 0; i < framesCount; i++) {
            currentFrames.add("");
            currentStatus.add("");
        }

        for (int i = 0; i < pageNumber; i++) {
            System.out.printf(" %2d ", pages[i]);
        }
        System.out.println("\n------------------------------------------------------------");

        for (int pageIndex = 0; pageIndex < pageNumber; pageIndex++) {
            boolean pageExists = map.getOrDefault(pages[pageIndex].toString(), false);
            //  System.out.println(pages[pageIndex] + " " + pageExists + " " + pointer);
            // Nếu trang chưa có trong bộ nhớ, thêm nó vào
            if (!pageExists) {
                faults[pageIndex] = true;
                if(currentStatus.get(pointer).isEmpty()) {
                    currentFrames.set(pointer, pages[pageIndex].toString()); // Thêm trang vào khung trang
                    map.put(pages[pageIndex].toString(), true);
                    currentStatus.set(pointer, "0");
                    pointer = (pointer + 1) % framesCount; // Di chuyển con trỏ
                    continue;
                }
                while (true) {
                    if (currentStatus.get(pointer).equals("0")) {
                        if(map.getOrDefault(currentFrames.get(pointer),false)) {
                            map.put(currentFrames.get(pointer), false);
                        }
                        currentFrames.set(pointer, pages[pageIndex].toString()); // Thay thế trang tại con trỏ
                        map.put(pages[pageIndex].toString(), true);
                        currentStatus.set(pointer, "0");
                        pointer = (pointer + 1) % framesCount; // Di chuyển con trỏ
                        break;
                    } else {
                        // Đặt lại bit tham chiếu và di chuyển con trỏ
                        currentStatus.set(pointer, "0");
                        pointer = (pointer + 1) % framesCount;
                    }
                }
            } else {
                for(int i = 0; i < currentFrames.size(); i++) {
                    if(currentFrames.get(i).equals(pages[pageIndex].toString())) {
                        currentStatus.set(i,"1");
                        pointer = (pointer + 1) % framesCount;
                        break;
                    }
                }
            }
            System.out.println(pages[pageIndex] + " " + pageExists + " " + pointer);
            for(int rowIndex = 0; rowIndex < framesCount; rowIndex++) {
                if(rowIndex < currentFrames.size()) {
                    displayFrames[rowIndex][pageIndex] = currentFrames.get(rowIndex);
                } else {
                    displayFrames[rowIndex][pageIndex] = "";
                }

                if(rowIndex == pointer) {
                    displayStatus[rowIndex][pageIndex] = String.format("*%s", currentStatus.get(rowIndex));
                } else {
                    displayStatus[rowIndex][pageIndex] = String.format("%s", currentStatus.get(rowIndex));
                }
            }
        }

        // In kết quả
        for(int i = 0; i < framesCount; i++) {
            for(int j = 0; j < pageNumber; j++) {
                System.out.printf(" %2s ", displayFrames[i][j] == null ? "" : displayFrames[i][j]);
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------------");
        for(int i = 0; i < pageNumber; i++) {
            System.out.printf(" %2s ", faults[i] ? "*" : " ");
        }
        System.out.println("\n------------------------------------------------------------");
        System.out.println();
        for(int i = 0; i < framesCount; i++) {
            for(int j = 0; j < pageNumber; j++) {
                System.out.printf(" %2s ", displayStatus[i][j] == null ? "" : displayStatus[i][j]);
            }
            System.out.println();
        }
    }
}
