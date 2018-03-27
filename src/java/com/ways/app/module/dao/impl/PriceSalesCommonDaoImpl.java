package com.ways.app.module.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.module.dao.IPriceSalesCommonDao;
import com.ways.app.module.entity.Level0Entity;
import com.ways.framework.base.IBatisBaseDao;
@Repository("priceSalesCommonDao")
public class PriceSalesCommonDaoImpl extends IBatisBaseDao implements IPriceSalesCommonDao {

	@Override
	public List getDate(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getDate");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Level0Entity> getSegment(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getSegment",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getMainManf(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getMainManf",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getAllManf(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getAllManf",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getAllBrand(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getAllBrand",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getMainBrand(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getMainBrand",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getAllManfBrand(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getAllManfBrand",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getMainManfBrand(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getMainManfBrand",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getSgmAndCompareModel(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getSgmAndCompareModel",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List getSegmentModel(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getSegmentModel",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getBrandModel(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getBrandModel",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getBodyType(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getBodyType",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getOriginal(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getOriginal",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getPooAttribute(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getPooAttribute",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getMaxAndMin(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getMaxAndMin",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getAreaCityData() {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getAreaCityData");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getProvinceData() {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getProvinceData");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getMaxSalesModelId(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getMaxSalesModelId",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getDefaultBrandId(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesCommon.getDefaultBrandId",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
