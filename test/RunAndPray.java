package edu.unice.polytech.kis.semwiktionary.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	edu.unice.polytech.kis.semwiktionary.database.DatabaseTest.class,
	edu.unice.polytech.kis.semwiktionary.model.WordTest.class
})

public class RunAndPray {}
