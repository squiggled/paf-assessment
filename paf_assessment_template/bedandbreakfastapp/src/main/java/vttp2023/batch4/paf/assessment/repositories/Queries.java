package vttp2023.batch4.paf.assessment.repositories;

public class Queries {
    public static final String SQL_CREATE_USER = """
                INSERT into users(email, name)
                VALUES (?, ?)
            """;
    public static final String SQL_CREATE_BOOKING = """
                INSERT into bookings(booking_id, listing_id, duration, email)
                VALUES (?, ?, ?, ?)
            """;
    public static final String SQL_FIND_USER="""
                SELECT * 
                FROM users 
                where email = ?
            """;

}
