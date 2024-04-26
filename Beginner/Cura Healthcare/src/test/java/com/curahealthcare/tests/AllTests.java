package com.curahealthcare.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	LoginTest.class,
	LogoutTest.class,
	MakeAppointmentTest.class,
	AppointmentHistoryTest.class,
})

public class AllTests {}
