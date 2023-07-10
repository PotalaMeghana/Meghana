package eStoreProduct.DAO.admin;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import eStoreProduct.model.admin.entities.adminModel;

@Component
public class adminDAOImp implements adminDAO {
		private static final Logger logger = LoggerFactory.getLogger(adminDAOImp.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public adminModel getAdmin(String email, String password) {
	    logger.info("Executing adminDAOImp.getAdmin() method. Searching for admin");
	    try {
	        logger.info("adminDAOImp public adminModel getAdmin() returns result");

	        String query = "SELECT a FROM adminModel a WHERE a.email = :email AND a.password = :password";

	        adminModel result = entityManager.createQuery(query, adminModel.class)
	                .setParameter("email", email)
	                .setParameter("password", password)
	                .getSingleResult();

	        logger.info("Admin found with email");
	        return result;
	    } catch (NoResultException e) {
	        logger.error("Error occurred in adminDAOImp.getAdmin() method. Searching for admin with email");

	        logger.warn("Admin not found with email");
	        return null; // Return null if admin is not found
	    } catch (Exception e) {
	        logger.error("Error occurred in adminDAOImp.getAdmin() method. Searching for admin with email");

	        // Handle the exception appropriately (e.g., logging, throwing custom exception, etc.)
	        e.printStackTrace();

	        logger.warn("Admin not found with email");
	        return null; // or throw an exception if required
	    }
	}

	
	@Override
	@Transactional
	public adminModel updatePassword(String password, String email) {
	    logger.info("Executing adminDAOImp.updatePassword() method. Updating password");
	    try {
	        String query = "update adminModel a set a.password=:password where a.email=:email";

	        int updatedCount = entityManager.createQuery(query)
	                .setParameter("email", email)
	                .setParameter("password", password)
	                .executeUpdate();

	        if (updatedCount > 0) {
	            logger.info("Admin password updated " + email);
	            return new adminModel(); // Return the updated adminModel instance or modify as per your requirements
	        } else {
	            logger.warn("Admin not found with email");
	            return null; // or throw an exception if required
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred in adminDAOImp.updatePassword() method. ");

	        e.printStackTrace();

	        logger.warn("Admin not found with email");
	        return null; // or throw an exception if required
	    }
	}


	@Override
	@Transactional
	public void updateAdmin(adminModel admin) {
		logger.info("Executing adminDAOImp.getAdmin() method. persisting admin");

		entityManager.merge(admin);
	}
}