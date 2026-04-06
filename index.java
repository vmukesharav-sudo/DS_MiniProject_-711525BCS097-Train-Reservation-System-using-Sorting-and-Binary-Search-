import java.util.*;

/**
 * Train Reservation System with Binary Search
 * Manages seat bookings dynamically with efficient sorting and searching
 */

// Class representing a train seat reservation record
class SeatReservation implements Comparable<SeatReservation> {
    private int seatNumber;
    private String passengerName;
    private boolean isBooked;
    private String bookingDate;
    private String trainId;

    public SeatReservation(int seatNumber, String passengerName, boolean isBooked, String bookingDate, String trainId) {
        this.seatNumber = seatNumber;
        this.passengerName = passengerName;
        this.isBooked = isBooked;
        this.bookingDate = bookingDate;
        this.trainId = trainId;
    }

    // Getters
    public int getSeatNumber() { return seatNumber; }
    public String getPassengerName() { return passengerName; }
    public boolean isBooked() { return isBooked; }
    public String getBookingDate() { return bookingDate; }
    public String getTrainId() { return trainId; }

    // Setters
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public void setBooked(boolean booked) { isBooked = booked; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    // Compare by seat number (for sorting and binary search)
    @Override
    public int compareTo(SeatReservation other) {
        return Integer.compare(this.seatNumber, other.seatNumber);
    }

    @Override
    public String toString() {
        return String.format("Seat %d | Passenger: %s | Status: %s | Date: %s | Train: %s",
                seatNumber, passengerName, isBooked ? "BOOKED" : "AVAILABLE", bookingDate, trainId);
    }
}

/**
 * Train Reservation Manager
 * Handles seat booking operations with binary search optimization
 */
class TrainReservationManager {
    private List<SeatReservation> seats;
    private final int totalSeats;
    private final String trainId;

    public TrainReservationManager(int totalSeats, String trainId) {
        this.totalSeats = totalSeats;
        this.trainId = trainId;
        this.seats = new ArrayList<>();
        initializeSeats();
    }

    // Initialize all seats as available
    private void initializeSeats() {
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new SeatReservation(i, "N/A", false, "N/A", trainId));
        }
    }

    /**
     * Binary search to find a seat by seat number
     * Time Complexity: O(log n)
     */
    public int binarySearchBySeatNumber(int seatNumber) {
        int left = 0, right = seats.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midSeatNumber = seats.get(mid).getSeatNumber();

            if (midSeatNumber == seatNumber) {
                return mid;
            } else if (midSeatNumber < seatNumber) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Seat not found
    }

    /**
     * Book a seat
     * Time Complexity: O(log n) for search + O(1) for booking
     */
    public boolean bookSeat(int seatNumber, String passengerName, String bookingDate) {
        if (seatNumber < 1 || seatNumber > totalSeats) {
            System.out.println("Invalid seat number!");
            return false;
        }

        int index = binarySearchBySeatNumber(seatNumber);

        if (index == -1) {
            System.out.println("Seat not found!");
            return false;
        }

        SeatReservation seat = seats.get(index);

        if (seat.isBooked()) {
            System.out.println("Seat " + seatNumber + " is already booked!");
            return false;
        }

        seat.setPassengerName(passengerName);
        seat.setBooked(true);
        seat.setBookingDate(bookingDate);
        System.out.println("Seat " + seatNumber + " booked successfully for " + passengerName);
        return true;
    }

    /**
     * Cancel a booking
     * Time Complexity: O(log n)
     */
    public boolean cancelBooking(int seatNumber) {
        int index = binarySearchBySeatNumber(seatNumber);

        if (index == -1) {
            System.out.println("Seat not found!");
            return false;
        }

        SeatReservation seat = seats.get(index);

        if (!seat.isBooked()) {
            System.out.println("Seat " + seatNumber + " is not booked!");
            return false;
        }

        seat.setPassengerName("N/A");
        seat.setBooked(false);
        seat.setBookingDate("N/A");
        System.out.println("Booking for seat " + seatNumber + " cancelled successfully");
        return true;
    }

    /**
     * Find all available seats in a range
     * Time Complexity: O(log n) for binary search + O(k) where k is number of available seats
     */
    public List<SeatReservation> findAvailableSeatsInRange(int startSeat, int endSeat) {
        List<SeatReservation> availableSeats = new ArrayList<>();

        int startIndex = binarySearchBySeatNumber(startSeat);
        if (startIndex == -1) {
            System.out.println("Start seat not found!");
            return availableSeats;
        }

        for (int i = startIndex; i < seats.size(); i++) {
            SeatReservation seat = seats.get(i);
            if (seat.getSeatNumber() > endSeat) break;
            if (!seat.isBooked()) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    /**
     * Find a booked seat by passenger name
     * Time Complexity: O(n) - linear search (no way to optimize with just seat number ordering)
     */
    public SeatReservation findBookingByPassenger(String passengerName) {
        for (SeatReservation seat : seats) {
            if (seat.isBooked() && seat.getPassengerName().equalsIgnoreCase(passengerName)) {
                return seat;
            }
        }
        return null;
    }

    /**
     * Get total booked and available seats count
     */
    public void displaySummary() {
        long bookedCount = seats.stream().filter(SeatReservation::isBooked).count();
        long availableCount = seats.size() - bookedCount;

        System.out.println("\n=== Reservation Summary ===");
        System.out.println("Train ID: " + trainId);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Booked Seats: " + bookedCount);
        System.out.println("Available Seats: " + availableCount);
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", (bookedCount * 100.0 / totalSeats)));
    }

    /**
     * Display all seat details (sorted by seat number)
     */
    public void displayAllSeats() {
        System.out.println("\n=== All Seats ===");
        for (SeatReservation seat : seats) {
            System.out.println(seat);
        }
    }

    /**
     * Display booked seats only
     */
    public void displayBookedSeats() {
        System.out.println("\n=== Booked Seats ===");
        seats.stream()
                .filter(SeatReservation::isBooked)
                .forEach(System.out::println);
    }

    /**
     * Display available seats only
     */
    public void displayAvailableSeats() {
        System.out.println("\n=== Available Seats ===");
        seats.stream()
                .filter(seat -> !seat.isBooked())
                .forEach(System.out::println);
    }

    /**
     * Check if a specific seat is available
     * Time Complexity: O(log n)
     */
    public boolean isSeatAvailable(int seatNumber) {
        int index = binarySearchBySeatNumber(seatNumber);
        if (index == -1) return false;
        return !seats.get(index).isBooked();
    }

    /**
     * Find consecutive available seats
     * Time Complexity: O(n) worst case, but typically much better
     */
    public List<SeatReservation> findConsecutiveAvailableSeats(int count) {
        List<SeatReservation> consecutive = new ArrayList<>();

        for (int i = 0; i < seats.size(); i++) {
            if (!seats.get(i).isBooked()) {
                consecutive.add(seats.get(i));
                if (consecutive.size() == count) {
                    return consecutive;
                }
            } else {
                consecutive.clear();
            }
        }

        if (consecutive.size() < count) {
            return new ArrayList<>();
        }
        return consecutive;
    }
}

/**
 * Main class to demonstrate the Train Reservation System
 */
public class index {
    public static void main(String[] args) {
        // Create a train with 100 seats
        TrainReservationManager train = new TrainReservationManager(100, "TR-001");

        System.out.println("=== Train Reservation System ===\n");

        // Book some seats
        train.bookSeat(5, "Alice Johnson", "2026-04-06");
        train.bookSeat(15, "Bob Smith", "2026-04-06");
        train.bookSeat(25, "Carol White", "2026-04-06");
        train.bookSeat(35, "David Brown", "2026-04-06");
        train.bookSeat(45, "Emma Davis", "2026-04-06");

        // Display summary
        train.displaySummary();

        // Find and display available seats in range
        System.out.println("\n=== Available Seats (1-50) ===");
        List<SeatReservation> availableInRange = train.findAvailableSeatsInRange(1, 50);
        availableInRange.forEach(System.out::println);

        // Find consecutive available seats
        System.out.println("\n=== Finding 5 Consecutive Available Seats ===");
        List<SeatReservation> consecutive = train.findConsecutiveAvailableSeats(5);
        if (!consecutive.isEmpty()) {
            consecutive.forEach(System.out::println);
        } else {
            System.out.println("Could not find 5 consecutive available seats");
        }

        // Check if specific seat is available
        System.out.println("\n=== Seat Availability Check ===");
        System.out.println("Seat 5 available: " + train.isSeatAvailable(5));
        System.out.println("Seat 10 available: " + train.isSeatAvailable(10));

        // Find booking by passenger name
        System.out.println("\n=== Find Booking by Passenger ===");
        SeatReservation booking = train.findBookingByPassenger("Alice Johnson");
        if (booking != null) {
            System.out.println("Found: " + booking);
        }

        // Display booked seats
        train.displayBookedSeats();

        // Cancel a booking
        System.out.println("\n=== Cancel Booking ===");
        train.cancelBooking(15);

        // Display updated summary
        train.displaySummary();

        // Try to book already booked seat
        System.out.println("\n=== Attempt to Book Already Booked Seat ===");
        train.bookSeat(5, "Frank Wilson", "2026-04-06");

        // Display final status
        train.displayBookedSeats();
        train.displaySummary();
    }
}
