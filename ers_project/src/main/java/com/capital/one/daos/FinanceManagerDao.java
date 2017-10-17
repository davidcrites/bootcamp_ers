package com.capital.one.daos;

import java.util.List;

import com.capital.one.datamodelbeans.Reimbursement;

public interface FinanceManagerDao {

    /***
     * Allows finance manager to get all employee reimbursement records in the ers reimbursement table
     * 
     * @return
     */

    List<Reimbursement> getAllEmployeeReimbursement();

    /***
     * Allows finance manager to filter by status type
     * 
     * @param status
     * @return
     */
    List<Reimbursement> filterByStatus(String status);

    /***
     * Allows finance manager to approve requests that are eligible for approval and pending
     * 
     * @param id
     * @return
     */
    boolean approveRequest(int id, int resolverId);

    /***
     * Allows finance manager to deny requests that are pending but not eligible for approval
     * 
     * @param id
     * @return
     */
    boolean denyRequest(int id, int resolverId);

}
