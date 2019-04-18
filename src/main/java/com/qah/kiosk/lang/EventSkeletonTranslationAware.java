package com.qah.kiosk.lang;

import java.util.List;

import com.qah.kiosk.domain.EventSkeleton;
import com.qah.kiosk.entity.EventTranslation;

public interface EventSkeletonTranslationAware extends EventSkeleton {

	public void setEventTranslations(List<EventTranslation> translations);
	public List<EventTranslation> getEventTranslations();
}
