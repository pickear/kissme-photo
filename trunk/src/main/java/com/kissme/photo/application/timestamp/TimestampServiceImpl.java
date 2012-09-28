package com.kissme.photo.application.timestamp;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kissme.photo.domain.timestamp.TimestampRepository;

/**
 * 
 * @author loudyn
 * 
 */
public class TimestampServiceImpl implements TimestampService {

	private TimestampRepository timestampRepository;

	@Inject
	public TimestampServiceImpl(TimestampRepository timestampRepository) {
		Preconditions.checkNotNull(timestampRepository);
		this.timestampRepository = timestampRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.application.TimestampService#save(java.lang.String)
	 */
	@Override
	public void save(String timestamp) {
		timestampRepository.save(timestamp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.application.TimestampService#existsTimestamp(java.lang.String)
	 */
	@Override
	public boolean existsTimestamp(String timestamp) {
		return timestampRepository.existsTimestamp(timestamp);
	}
}
