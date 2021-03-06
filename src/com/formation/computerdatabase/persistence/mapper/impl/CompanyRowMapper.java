package com.formation.computerdatabase.persistence.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.formation.computerdatabase.model.Company;
import com.formation.computerdatabase.persistence.mapper.RowMapper;

public class CompanyRowMapper implements RowMapper<Company> {

	@Override
	public Company mapRow(ResultSet rs) throws SQLException {
		Company company = new Company();
		if (rs == null || !rs.next()) {
			return company;
		}
		company.setId(rs.getLong(COMPANY_PREFIX + "id"));
		company.setName(rs.getString(COMPANY_PREFIX + "name"));
		return company;
	}

	@Override
	public List<Company> mapRows(ResultSet rs) throws SQLException {
		List<Company> companies = new ArrayList<Company>();
		if (rs == null || !rs.next()) {
			return companies;
		}
		do {
			Company company = new Company();

			company.setId(rs.getLong(COMPANY_PREFIX + "id"));
			company.setName(rs.getString(COMPANY_PREFIX + "name"));
			companies.add(company);
		} while (rs.next());
		return companies;
	}

}
