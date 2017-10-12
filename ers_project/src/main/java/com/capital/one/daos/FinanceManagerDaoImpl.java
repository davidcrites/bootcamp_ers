package com.capital.one.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.capital.one.datamodelbeans.Reimbursement;

public class FinanceManagerDaoImpl implements FinanceManagerDao {

    public static void main(String[] args) {
        filterByStatus("Pending");

    }

    /***
     * Gets All Records from ERS Reimbursement Tablet
     * 
     * @return
     */

    public static List<Reimbursement> getAllEmployeeReimbursement() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres?currentSchema=public", "postgres",
                    "");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM \"ers_reimbursement\"");
            List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
            while (rs.next()) {
                Reimbursement reimbursement = new Reimbursement();
                reimbursement.setReimbusementId(rs.getInt("reimb_id"));// populate the object
                reimbursement.setReimbursementAmount(rs.getDouble("reimb_amount"));
                reimbursement.setReimbSubmitted(rs.getTimestamp("reimb_submitted").toLocalDateTime());
                reimbursement.setReimbResolved(rs.getTimestamp("reimb_resolved").toLocalDateTime());
                reimbursement.setReimbDescription(rs.getString("reimb_description"));
                reimbursement.setAuthorId(rs.getInt("reimb_author"));
                reimbursement.setReimbResolver(rs.getInt("reimb_resolver"));
                reimbursement.setReimbStatusId(rs.getInt("reimb_status_id"));
                reimbursement.setReimbTypeId(rs.getInt("reimb_type_id"));

                reimbursementList.add(reimbursement);
                System.out.println(reimbursementList);
                return reimbursementList;

            }

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Filters the records by Status
     * 
     * @param status
     * @return
     */

    public static List<Reimbursement> filterByStatus(String status) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres?currentSchema=public", "postgres",
                    "");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt
                    .executeQuery("Select table1.\"reimb_id\", table1.\"reimb_amount\", table1.\"reimb_submitted\"," +
                            "table1.\"reimb_resolved\", table1.\"reimb_description\", table1.\"reimb_receipt\", table1.\"reimb_author\", table1.\"reimb_resolver\","
                            +
                            " table2.\"reimb_status\" From \"ers_reimbursement\" table1 Inner Join \"ers_reimbursement_status\" table2"
                            +
                            " on table2.\"reimb_status_id\"=  table1.\"reimb_status_id\" where table2.\"reimb_status\" ='"
                            + status + "'");
            List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();

            while (rs.next()) {
                Reimbursement reimbursement = new Reimbursement();
                reimbursement.setReimbusementId(rs.getInt("reimb_id"));// populate the object
                reimbursement.setReimbursementAmount(rs.getDouble("reimb_amount"));
                reimbursement.setReimbSubmitted(rs.getTimestamp("reimb_submitted").toLocalDateTime());
                reimbursement.setReimbResolved(rs.getTimestamp("reimb_resolved").toLocalDateTime());
                reimbursement.setReimbDescription(rs.getString("reimb_description"));
                reimbursement.setAuthorId(rs.getInt("reimb_author"));
                reimbursement.setReimbResolver(rs.getInt("reimb_resolver"));
                reimbursement.getStatus().setReimbStatus(rs.getString("reimb_status"));

                reimbursementList.add(reimbursement);
                System.out.println(reimbursementList);
                return reimbursementList;

            }

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;

    }

    public void approveDenyRequest() {

    }

}
