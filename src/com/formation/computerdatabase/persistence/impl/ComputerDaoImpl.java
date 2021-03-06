package com.formation.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.formation.computerdatabase.exception.PersistenceException;
import com.formation.computerdatabase.model.Computer;
import com.formation.computerdatabase.persistence.ComputerDao;
import com.formation.computerdatabase.persistence.EntityManagerFactory;
import com.formation.computerdatabase.persistence.mapper.RowMapper;
import com.formation.computerdatabase.persistence.mapper.impl.ComputerRowMapper;

public class ComputerDaoImpl implements ComputerDao {

	private RowMapper<Computer> computerRowMapper = new ComputerRowMapper();

	private static final String CREATE = "insert into computer(name, introduced, discontinued, company_id) values (?, ?, ?, ?);";

	@Override
	public void create(Computer computer) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = EntityManagerFactory.INSTANCE.getConnection();
			stmt = conn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

			stmt.setString(1, computer.getName());
			if( computer.getIntroduced() != null ) {
				stmt.setDate(2, new Date(computer.getIntroduced().getTime()));
			} else {
				stmt.setNull(2, java.sql.Types.NULL);
			}
			if(computer.getDiscontinued() != null) {
				stmt.setDate(3, new Date(computer.getDiscontinued().getTime()));
			} else {
				stmt.setNull(3, java.sql.Types.NULL);
			}
			if(computer.getCompany() != null) {
				stmt.setLong(4, computer.getCompany().getId());
			} else {
				stmt.setNull(4, java.sql.Types.NULL);
			}
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			
			if (rs.next()) {
				computer.setId(rs.getLong(1));
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			EntityManagerFactory.INSTANCE.closeConnection(conn, stmt, rs);
		}
	}

	private static final String RETRIEVE_ALL = "select cp.id, cp.name, cp.introduced, cp.discontinued, ca.id, ca.name from computer cp left join company ca on cp.company_id = ca.id;";

	@Override
	public List<Computer> retrieveAll() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Computer> computers = null;

		try {
			conn = EntityManagerFactory.INSTANCE.getConnection();
			stmt = conn.prepareStatement(RETRIEVE_ALL);
			rs = stmt.executeQuery();
			computers = computerRowMapper.mapRows(rs);
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			EntityManagerFactory.INSTANCE.closeConnection(conn, stmt, rs);
		}
		return computers;
	}

	private static final String RETRIEVE_ONE = "select cp.id, cp.name, cp.introduced, cp.discontinued, ca.id, ca.name from computer cp left join company ca on cp.company_id = ca.id where cp.id = ?;";

	@Override
	public Computer retrieveOne(Long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Computer computer = null;

		try {
			conn = EntityManagerFactory.INSTANCE.getConnection();
			stmt = conn.prepareStatement(RETRIEVE_ONE);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			computer = computerRowMapper.mapRow(rs);
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			EntityManagerFactory.INSTANCE.closeConnection(conn, stmt, rs);
		}
		return computer;
	}

	private static final String UPDATE = "update computer set name = ?, introduced = ?, discontinued = ?, company_id = ? where id = ?";

	@Override
	public void update(Computer computer) {
		Connection conn = null;
		PreparedStatement stmt = null;
		if (computer != null) {
			try {
				conn = EntityManagerFactory.INSTANCE.getConnection();
				stmt = conn.prepareStatement(UPDATE);
				
				stmt.setString(1, computer.getName());
				if( computer.getIntroduced() != null ) {
					stmt.setDate(2, new Date(computer.getIntroduced().getTime()));
				} else {
					stmt.setNull(2, java.sql.Types.NULL);
				}
				if(computer.getDiscontinued() != null) {
					stmt.setDate(3, new Date(computer.getDiscontinued().getTime()));
				} else {
					stmt.setNull(3, java.sql.Types.NULL);
				}
				if(computer.getCompany() != null) {
					stmt.setLong(4, computer.getCompany().getId());
				} else {
					stmt.setNull(4, java.sql.Types.NULL);
				}
				stmt.setLong(5, computer.getId());
				stmt.executeUpdate();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage(), e);
			} finally {
				EntityManagerFactory.INSTANCE.closeConnection(conn, stmt);
			}
		}
	}
	
	private static final String DELETE = "delete computer cp where cp.id= ?";
		
	@Override
	public void delete(Long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		if (id != null) {
			try {
				conn = EntityManagerFactory.INSTANCE.getConnection();
				stmt = conn.prepareStatement(DELETE);
				stmt.setLong(1, id);
				stmt.executeUpdate();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage(), e);
			} finally {
				EntityManagerFactory.INSTANCE.closeConnection(conn, stmt);
			}
		}
	}

	@Override
	public void delete(List<Long> ids) {
		for(Long id: ids) {
			this.delete(id);
		}
	}

}
