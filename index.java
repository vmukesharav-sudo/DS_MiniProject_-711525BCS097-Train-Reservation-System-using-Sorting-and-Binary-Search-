import java.util.*;

class Passenger {
    int id;
    String name;
    int age;
    String seat;
    int trainNo;

    Passenger(int id, String name, int age, String seat, int trainNo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.seat = seat;
        this.trainNo = trainNo;
    }
}

public class index {

    static ArrayList<Passenger> list = new ArrayList<Passenger>();
    static Scanner sc = new Scanner(System.in);
    static boolean isSorted = false;

    // Insert Data (FIXED INPUT)
    static void insertData() {
        System.out.println("Enter ID:");
        int id = sc.nextInt();
        sc.nextLine(); // clear buffer

        // Check duplicate ID
        for (Passenger p : list) {
            if (p.id == id) {
                System.out.println("ID already exists!");
                return;
            }
        }

        System.out.println("Enter Name:");
        String name = sc.nextLine();  // ✅ full name allowed

        System.out.println("Enter Age:");
        int age = sc.nextInt();
        sc.nextLine(); // clear buffer

        System.out.println("Enter Seat:");
        String seat = sc.nextLine();

        System.out.println("Enter Train Number:");
        int trainNo = sc.nextInt();

        list.add(new Passenger(id, name, age, seat, trainNo));
        isSorted = false;

        System.out.println("Passenger Added!");
    }

    // Display Data
    static void display() {
        if (list.size() == 0) {
            System.out.println("No Records Found");
            return;
        }

        System.out.println("\nID\tName\t\tSeat\tTrainNo");
        for (Passenger p : list) {
            System.out.println(p.id + "\t" + p.name + "\t" + p.seat + "\t" + p.trainNo);
        }
    }

    // Sort Data (Insertion Sort)
    static void sortData() {
        for (int i = 1; i < list.size(); i++) {
            Passenger key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).id > key.id) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }

        isSorted = true;
        System.out.println("Sorted Successfully!");
    }

    // Binary Search
    static void searchData() {
        if (!isSorted) {
            System.out.println("Please sort data first!");
            return;
        }

        System.out.println("Enter ID to search:");
        int key = sc.nextInt();

        int low = 0, high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (list.get(mid).id == key) {
                Passenger p = list.get(mid);
                System.out.println("Found: " + p.name + " | Seat: " + p.seat);
                return;
            } else if (list.get(mid).id < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("Not Found");
    }

    // Delete Data
    static void deleteData() {
        System.out.println("Enter ID to delete:");
        int key = sc.nextInt();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == key) {
                list.remove(i);
                System.out.println("Deleted Successfully!");
                return;
            }
        }

        System.out.println("ID not found!");
    }

    // Main Menu
    public static void main(String[] args) {

        int choice;

        while (true) {
            System.out.println("\n1.Insert 2.Display 3.Sort 4.Search 5.Delete 6.Exit");
            System.out.println("Enter your choice:");

            if (!sc.hasNextInt()) break;
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    insertData();
                    break;
                case 2:
                    display();
                    break;
                case 3:
                    sortData();
                    break;
                case 4:
                    searchData();
                    break;
                case 5:
                    deleteData();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}