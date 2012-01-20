package edu.unice.polytech.kis.semwiktionary.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	edu.unice.polytech.kis.semwiktionary.database.DatabaseTest.class,
	edu.unice.polytech.kis.semwiktionary.model.WordTest.class,
	edu.unice.polytech.kis.semwiktionary.model.DefinitionTest.class,
	edu.unice.polytech.kis.semwiktionary.model.MutableWordTest.class
})

public class RunAndPray {}
