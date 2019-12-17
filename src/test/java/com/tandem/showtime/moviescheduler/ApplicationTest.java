package com.tandem.showtime.moviescheduler;



import org.junit.jupiter.api.Test;

import com.tandem.showtime.moviescheduler.utils.TestConstants;

public class ApplicationTest extends ScheduleGenerationTestUtils{

	@Test
	public void testMain() {
		Application.main(TestConstants.ARGS);
		super.checkThatScheduleWasGenerated();
	}
}
