package com.capital.one.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.capital.one.datamodelbeans.Reimbursement;

public class FinanceManagerDaoImpl implements FinanceManagerDao {

    /***
     * Gets All Records from ERS Reimbursement Tablet
     * 
     * @return
     */

    Logger log = Logger.getLogger("FinanceManagerDaoImpl");

    Calendar calendar = Calendar.getInstance();

    java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

    public List<Reimbursement> getAllEmployeeReimbursement() {

        try {

            Connection conn = DAOUtilities.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM \"ers_reimbursement\"");
            List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();

            while (rs.next()) {
                log.trace("reimb_id = " + rs.getInt("reimb_id"));
                Reimbursement reimbursement = new Reimbursement();
                reimbursement.setReimbusementId(rs.getInt("reimb_id"));// populate the object
                reimbursement.setReimbursementAmount(rs.getDouble("reimb_amount"));
                reimbursement.setReimbSubmitted(rs.getTimestamp("reimb_submitted").toLocalDateTime());
                if (rs.getTimestamp("reimb_resolved") != null) {
                    reimbursement.setReimbResolved(rs.getTimestamp("reimb_resolved").toLocalDateTime());
                }
                else {
                    reimbursement.setReimbResolved(null);
                }
                reimbursement.setReimbDescription(rs.getString("reimb_description"));
                reimbursement.setReimbAuthor(rs.getInt("reimb_author"));
                reimbursement.setReimbResolver(rs.getInt("reimb_resolver"));
                reimbursement.setReimbStatusId(rs.getInt("reimb_status_id"));
                reimbursement.setReimbTypeId(rs.getInt("reimb_type_id"));
                reimbursementList.add(reimbursement);

            }

            log.trace(reimbursementList);
            return reimbursementList;

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

    public List<Reimbursement> filterByStatus(String status) {
        try {

            Connection conn = DAOUtilities.getConnection();

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
                if (rs.getTimestamp("reimb_resolved") != null) {
                    reimbursement.setReimbResolved(rs.getTimestamp("reimb_resolved").toLocalDateTime());
                }
                else {
                    reimbursement.setReimbResolved(null);
                }
                reimbursement.setReimbDescription(rs.getString("reimb_description"));
                reimbursement.setReimbAuthor(rs.getInt("reimb_author"));
                reimbursement.setReimbResolver(rs.getInt("reimb_resolver"));
                reimbursement.getStatus().setReimbStatus(rs.getString("reimb_status"));

                reimbursementList.add(reimbursement);
            }
            log.trace(reimbursementList);
            return reimbursementList;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;

    }

    public boolean approveRequest(int id, int resolverId) {
        try {

            Connection conn = DAOUtilities.getConnection();

            Statement stmt = conn.createStatement();

            stmt.executeUpdate(
                    "UPDATE ers_reimbursement SET reimb_status_id=2, resolved_time=CURRENT_TIMESTAMP, reimb_resolver="
                            + resolverId +
                            "WHERE reimb_status_id=1 And reimb_id=" + id);
            return true;
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }

    }

    public boolean denyRequest(int id, int resolverId) {
        try {

            Connection conn = DAOUtilities.getConnection();

            Statement stmt = conn.createStatement();

            stmt.executeUpdate(
                    "UPDATE ers_reimbursement SET reimb_status_id=3, reimb_resolved=CURRENT_TIMESTAMP, reimb_resolver="
                            + resolverId +
                            "WHERE reimb_status_id=1 And reimb_id=" + id);
            return true;
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }

    }

}