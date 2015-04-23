<<<<<<< HEAD
DELIMITER //
	DROP PROCEDURE IF EXISTS doClientSubStatusUpdates;
	CREATE DEFINER=`root`@`localhost` PROCEDURE `doClientSubStatusUpdates`()
BEGIN
		-- Initialize variables
		DECLARE numMonths_WithRegularDeposits_forActiveGood INT DEFAULT 6;DECLARE numMonths_for_Dormancy INT DEFAULT 4;DECLARE numMonths_for_Default INT DEFAULT 4;DECLARE newClient_CodeValue INT DEFAULT 26;DECLARE activeGoodClient_CodeValue INT DEFAULT 27;DECLARE dormantClient_CodeValue INT DEFAULT 28;DECLARE defaultClient_CodeValue INT DEFAULT 29;-- Set values for above variables so that the installation specific values are set
		
		SELECT value INTO numMonths_WithRegularDeposits_forActiveGood FROM c_configuration 
				WHERE name = 'Num-Months-WithDeposits-ForGoodStandingSubStatus';SELECT value INTO numMonths_for_Dormancy FROM c_configuration 
				WHERE name = 'Num-Months-WithoutDeposits-ForDormancySubStatus';SELECT value INTO numMonths_for_Default FROM c_configuration 
				WHERE name = 'Num-Months-WithoutRepayment-ForDefaultSubStatus';SELECT id INTO newClient_CodeValue FROM m_code_value WHERE 
			code_id = (SELECT id FROM m_code WHERE code_name = 'ClientSubStatus') AND
			code_value = 'New Client';SELECT id INTO activeGoodClient_CodeValue FROM m_code_value WHERE 
			code_id = (SELECT id FROM m_code WHERE code_name = 'ClientSubStatus') AND
			code_value = 'Active in Good Standing';SELECT id INTO dormantClient_CodeValue FROM m_code_value WHERE 
			code_id = (SELECT id FROM m_code WHERE code_name = 'ClientSubStatus') AND
			code_value = 'Dormant';SELECT id INTO defaultClient_CodeValue FROM m_code_value WHERE 
			code_id = (SELECT id FROM m_code WHERE code_name = 'ClientSubStatus') AND
			code_value = 'Default';-- a) All "null" sub-status to be updated to "New"
=======
DELIMITER $$
	DROP PROCEDURE IF EXISTS doClientSubStatusUpdates;
	CREATE PROCEDURE doClientSubStatusUpdates ()
	BEGIN
		DECLARE numMonths_WithRegularDeposits_forActiveGood INT DEFAULT 6;
		DECLARE numMonths_for_Dormancy INT DEFAULT 3;
		DECLARE numMonths_for_Default INT DEFAULT 4;
		
		DECLARE newClient_CodeValue INT DEFAULT 13;
		DECLARE activeGoodClient_CodeValue INT DEFAULT 14;
		DECLARE dormantClient_CodeValue INT DEFAULT 15;
		DECLARE defaultClient_CodeValue INT DEFAULT 16;

		-- a) All "null" sub-status to be updated to "New"
>>>>>>> caritasrepo/sub_status_custom_batch_job
	
		UPDATE m_client
			SET sub_status = newClient_CodeValue 
			WHERE sub_status IS NULL AND 
<<<<<<< HEAD
				status_enum = 300;-- b) select all "new" clients with regular deposits for last 6 months
=======
				status_enum = 300;
		
		-- b) select all "new" clients with regular deposits for last 6 months
>>>>>>> caritasrepo/sub_status_custom_batch_job
		--	Change sub-status to "active in good standing"
	
		UPDATE m_client client 
		JOIN
		(
			SELECT client_sav_txns.client_id AS client_id, client_name, COUNT(*) kount FROM (
				SELECT sav.id, sav.client_id, client.display_name AS client_name, MONTH(sav_txn.transaction_date), COUNT( * ) 
				FROM  `m_savings_account` sav
					LEFT OUTER JOIN `m_savings_account_transaction` sav_txn on sav.id = sav_txn.savings_account_id
					LEFT JOIN `m_client` client on sav.client_id = client.id
<<<<<<< HEAD
					LEFT JOIN client_sub_status_migration_date migration on migration.client_id=client.id
=======
>>>>>>> caritasrepo/sub_status_custom_batch_job
				WHERE
					client.sub_status = newClient_CodeValue AND 
					client.status_enum = 300 AND
					sav.status_enum = 300 AND 
<<<<<<< HEAD
					sav.product_id=1 AND
					sav_txn.transaction_type_enum = 1 AND
 					if(NOW() >= DATE_ADD(IFNULL(migration.migrated_date,'2099-01-01'),INTERVAL numMonths_WithRegularDeposits_forActiveGood MONTH),
					sav_txn.transaction_date BETWEEN migration.migrated_date AND DATE_ADD(migration.migrated_date,INTERVAL numMonths_WithRegularDeposits_forActiveGood MONTH),
					sav_txn.transaction_date <= now())
=======
					sav_txn.transaction_type_enum = 1 AND
					sav_txn.transaction_date > DATE_SUB(now(), INTERVAL numMonths_WithRegularDeposits_forActiveGood MONTH)
>>>>>>> caritasrepo/sub_status_custom_batch_job
				GROUP BY 
					sav.id, sav.client_id, MONTH( sav_txn.transaction_date ) 
				) client_sav_txns
			GROUP BY client_sav_txns.client_id
			HAVING COUNT(*) >= numMonths_WithRegularDeposits_forActiveGood
			) client_monthly_sav_txns
			ON client.id = client_monthly_sav_txns.client_id
<<<<<<< HEAD
		SET client.sub_status = activeGoodClient_CodeValue;-- c) select all "dormant" clients with at least 1 deposit in last 3 months
		--	Change sub-status to "active in good standing"
		
=======
		SET client.sub_status = activeGoodClient_CodeValue;
		
		-- c) select all "dormant" clients with at least 1 deposit in last 3 months
		--	Change sub-status to "active in good standing"
>>>>>>> caritasrepo/sub_status_custom_batch_job
		UPDATE m_client client 
		JOIN
		(
			SELECT client_sav_txns.client_id AS client_id, client_name, COUNT(*) kount FROM (
				SELECT sav.id, sav.client_id, client.display_name AS client_name, MONTH(sav_txn.transaction_date), COUNT( * ) 
				FROM  `m_savings_account` sav
					LEFT OUTER JOIN `m_savings_account_transaction` sav_txn on sav.id = sav_txn.savings_account_id
					LEFT JOIN `m_client` client on sav.client_id = client.id
				WHERE
					client.sub_status = dormantClient_CodeValue AND 
					client.status_enum = 300 AND
<<<<<<< HEAD
					sav.status_enum = 300 AND
					sav.product_id=1 AND 
					sav_txn.transaction_type_enum = 1 AND
					sav_txn.transaction_date > DATE_SUB(now(), INTERVAL numMonths_for_Dormancy MONTH)
=======
					sav.status_enum = 300 AND 
					sav_txn.transaction_type_enum = 1 AND
					sav_txn.transaction_date > DATE_SUB(now(), INTERVAL numMonths_WithRegularDeposits_forActiveGood MONTH)
>>>>>>> caritasrepo/sub_status_custom_batch_job
				GROUP BY 
					sav.id, sav.client_id, MONTH( sav_txn.transaction_date ) 
				) client_sav_txns
			GROUP BY client_sav_txns.client_id
			HAVING COUNT(*) >= 1
			) client_monthly_sav_txns
			ON client.id = client_monthly_sav_txns.client_id
<<<<<<< HEAD
		SET client.sub_status = activeGoodClient_CodeValue;-- d-1) select all "default" clients with all loans that are closed
		--	Change sub-status to "active in good standing"
		
		UPDATE m_client client 
		JOIN
			(
				SELECT l.client_id, min(duedate) min_due_date
					FROM m_loan l
					LEFT JOIN m_client client ON l.client_id = client.id
					LEFT JOIN m_loan_repayment_schedule mlrs ON l.id = mlrs.loan_id
				WHERE
					client.sub_status = defaultClient_CodeValue AND
					client.status_enum = 300 AND
					l.loan_status_id = 600 AND
					l.client_id NOT IN (
						SELECT l.client_id client_id
							FROM m_loan l
							LEFT JOIN m_client client ON l.client_id = client.id
							LEFT JOIN m_loan_repayment_schedule mlrs ON l.id = mlrs.loan_id
						WHERE
							client.sub_status = defaultClient_CodeValue AND
							client.status_enum = 300 AND
							l.loan_status_id  = 300
							GROUP BY l.client_id
					)
					GROUP BY l.client_id
			) overdueclients_withobligationsmet
			ON client.id = overdueclients_withobligationsmet.client_id
		SET sub_status = activeGoodClient_CodeValue
		WHERE client.sub_status = defaultClient_CodeValue;-- d-2) select all "default" clients with loans with the least due date > current date
		--	Change sub-status to "active in good standing"
		
=======
		SET client.sub_status = activeGoodClient_CodeValue;
		
		-- d) select all "default" clients with loans with the least due date > current date
		--	Change sub-status to "active in good standing"
>>>>>>> caritasrepo/sub_status_custom_batch_job
		UPDATE m_client client 
		JOIN
			(
				SELECT l.client_id, min(duedate) min_due_date
					FROM m_loan l
					LEFT JOIN m_client client ON l.client_id = client.id
					LEFT JOIN m_loan_repayment_schedule mlrs ON l.id = mlrs.loan_id
				WHERE
					client.sub_status = defaultClient_CodeValue AND
					client.status_enum = 300 AND
					l.loan_status_id = 300 AND
					mlrs.obligations_met_on_date IS NULL
					GROUP BY l.client_id
			) overdueclients_withobligationsmet
			ON client.id = overdueclients_withobligationsmet.client_id
		SET sub_status = activeGoodClient_CodeValue
		WHERE client.sub_status = defaultClient_CodeValue AND
<<<<<<< HEAD
			overdueclients_withobligationsmet.min_Due_date > CURDATE();-- e) select "active in good standing" clients with at least one active loan
=======
			overdueclients_withobligationsmet.min_Due_date > CURDATE();
		
		-- e) select "active in good standing" clients with at least one active loan
>>>>>>> caritasrepo/sub_status_custom_batch_job
		--	check if they have any loans that with at least 4 months of overdues,
		--		if yes change status to "default"
		
		UPDATE m_client client 
		JOIN
			(
				SELECT DISTINCT client_id 
					FROM m_loan l
					LEFT JOIN m_client client ON l.client_id = client.id
					LEFT JOIN m_loan_repayment_schedule mlrs ON l.id = mlrs.loan_id
				WHERE
					client.sub_status = activeGoodClient_CodeValue AND
					client.status_enum = 300 AND
					l.loan_status_id = 300 AND
					mlrs.obligations_met_on_date IS NULL AND
					duedate < date_sub(CURDATE(),INTERVAL numMonths_for_Default MONTH)
			) overdueclients
			ON client.id = overdueclients.client_id
<<<<<<< HEAD
		SET sub_status = defaultClient_CodeValue;--	else, check if there is at least one deposit in last 3 months
=======
		SET sub_status = defaultClient_CodeValue;
		
		--	else, check if there is at least one deposit in last 3 months
>>>>>>> caritasrepo/sub_status_custom_batch_job
		--		if yes, no change
		--		if not, change status to "dormant" with appropriate sub_status_change_date
		
		UPDATE m_client client 
		JOIN
		(
			SELECT DISTINCT client.id AS client_id FROM m_client client
				LEFT JOIN `m_savings_account` sav ON sav.client_id = client.id
			WHERE
				client.sub_status = activeGoodClient_CodeValue AND 
				sav.status_enum = 300 AND
<<<<<<< HEAD
				sav.product_id=1 AND
=======
>>>>>>> caritasrepo/sub_status_custom_batch_job
				client.id NOT IN (
					SELECT DISTINCT client_sav_txns.client_id AS client_id FROM (
						SELECT sav.id, sav.client_id, MONTH(sav_txn.transaction_date) AS MONTH, COUNT(*) 
						FROM  `m_client` client
							LEFT OUTER JOIN `m_savings_account` sav ON sav.client_id = client.id
							LEFT OUTER JOIN `m_savings_account_transaction` sav_txn ON sav.id = sav_txn.savings_account_id
<<<<<<< HEAD
							LEFT JOIN client_sub_status_migration_date migration on migration.client_id=client.id
=======
>>>>>>> caritasrepo/sub_status_custom_batch_job
						WHERE
							client.sub_status = activeGoodClient_CodeValue AND 
							client.status_enum = 300 AND
							sav.status_enum = 300 AND 
<<<<<<< HEAD
							sav.product_id = 1 AND
							sav_txn.transaction_type_enum = 1 AND
							if(NOW() >= DATE_ADD(IFNULL(migration.migrated_date,'2099-01-01'),INTERVAL numMonths_for_Dormancy MONTH),
					sav_txn.transaction_date BETWEEN migration.migrated_date AND DATE_ADD(migration.migrated_date,INTERVAL numMonths_for_Dormancy MONTH),
					sav_txn.transaction_date > DATE_SUB(now(), INTERVAL numMonths_for_Dormancy MONTH))
=======
							sav_txn.transaction_type_enum = 1 AND
							sav_txn.transaction_date > DATE_SUB(now(), INTERVAL numMonths_for_Dormancy MONTH)
>>>>>>> caritasrepo/sub_status_custom_batch_job
						GROUP BY 
							sav.id, sav.client_id, MONTH( sav_txn.transaction_date ) 
					) client_sav_txns
					GROUP BY client_sav_txns.client_id
					HAVING count(*) > 0
				)
		) client_without_monthly_sav_txns_for_X_months
		ON client.id = client_without_monthly_sav_txns_for_X_months.client_id
<<<<<<< HEAD
		SET client.sub_status = dormantClient_CodeValue;delete from client_sub_status_migration_date
		where sub_status=newClient_CodeValue and migrated_date <= DATE_ADD(now(),INTERVAL numMonths_WithRegularDeposits_forActiveGood MONTH);delete from client_sub_status_migration_date
		where sub_status=activeGoodClient_CodeValue and migrated_date <= DATE_ADD(now(),INTERVAL numMonths_for_Default MONTH);delete from client_sub_status_migration_date
		where sub_status=defaultClient_CodeValue and migrated_date <= DATE_ADD(now(),INTERVAL numMonths_for_Dormancy MONTH);END//
DELIMITER ;
=======
		SET client.sub_status = dormantClient_CodeValue;

	END $$
DELIMITER ;
 
>>>>>>> caritasrepo/sub_status_custom_batch_job
