package com.bai.env.region;

import com.bai.util.Architecture;
import com.bai.util.Config;
import com.bai.util.GlobalState;
import com.bai.util.Logging;
import ghidra.program.database.ProgramBuilder;
import ghidra.program.database.ProgramDB;
import ghidra.program.flatapi.FlatProgramAPI;
import ghidra.program.model.lang.Language;
import ghidra.program.model.lang.Processor;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.Memory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class LocalTest {

    protected Program program;
    protected ProgramBuilder programBuilder;

    @BeforeClass
    public static void initClass() {
        GlobalState.config = new Config();
        Logging.init();
    }

    @Before
    public void init() throws Exception {
        programBuilder = new ProgramBuilder("Test", ProgramBuilder._ARM);
        program = programBuilder.getProgram();
        int txId = program.startTransaction("Add memory");
        programBuilder.createMemory(".text", "0x1000", 0x100).setExecute(true);
        program.endTransaction(txId, true);
        programBuilder.analyze();
        GlobalState.currentProgram = program;
        GlobalState.flatAPI = new FlatProgramAPI(program);
        GlobalState.reset();
        GlobalState.arch = new Architecture(program);
        GlobalState.config = Mockito.mock(Config.class);
    }
    @Test
    public void testGetLocal() {
        Function func1 = Mockito.mock(Function.class);

        Local local1 = Local.getLocal(func1);
        Local local2 = Local.getLocal(func1);
        assert local1 == local2;

        Local.resetPool();
        Local local3 = Local.getLocal(func1);
        assert local1 != local3;
        assert local1.equals(local3);
        assert local3.getFunction() == func1;
    }
}