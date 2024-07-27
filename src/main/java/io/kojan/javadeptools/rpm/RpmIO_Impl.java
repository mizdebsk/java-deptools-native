package io.kojan.javadeptools.rpm;

import io.kojan.javadeptools.nativ.AbstractNativeProxy;
import io.kojan.javadeptools.rpm.Rpm.RpmEVR;
import io.kojan.javadeptools.rpm.Rpm.RpmFD;
import io.kojan.javadeptools.rpm.Rpm.RpmIO;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

/**
 * Native implementation of RpmIO.
 */
final class RpmIO_Impl extends AbstractNativeProxy implements RpmIO {
    public RpmIO_Impl(SymbolLookup lookup) {
        super(lookup);
    }

    /**
     * Method stub that invokes native method {@code Fclose}.
     * @param fd RpmFD
     */
    @Override
    public void Fclose(RpmFD fd) {
        try {
            mh_Fclose.invokeExact(
                    downConvertObject(fd)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fclose", _t);
        }
    }

    private final MethodHandle mh_Fclose = makeMethodHandle(VOID, "Fclose", OBJ);

    /**
     * Method stub that invokes native method {@code Ferror}.
     * @param fd RpmFD
     * @return int
     */
    @Override
    public int Ferror(RpmFD fd) {
        try {
            return (int)mh_Ferror.invokeExact(
                    downConvertObject(fd)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Ferror", _t);
        }
    }

    private final MethodHandle mh_Ferror = makeMethodHandle(INT, "Ferror", OBJ);

    /**
     * Method stub that invokes native method {@code Fopen}.
     * @param path String
     * @param mode String
     * @return RpmFD
     */
    @Override
    public RpmFD Fopen(String path, String mode) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(RpmFD::new, (MemorySegment)mh_Fopen.invokeExact(
                    downConvertString(path, arena),
                    downConvertString(mode, arena)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fopen", _t);
        }
    }

    private final MethodHandle mh_Fopen = makeMethodHandle(OBJ, "Fopen", STR, STR);

    /**
     * Method stub that invokes native method {@code Fstrerror}.
     * @param fd RpmFD
     * @return String
     */
    @Override
    public String Fstrerror(RpmFD fd) {
        try {
            return upConvertString((MemorySegment)mh_Fstrerror.invokeExact(
                    downConvertObject(fd)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fstrerror", _t);
        }
    }

    private final MethodHandle mh_Fstrerror = makeMethodHandle(STR, "Fstrerror", OBJ);

    /**
     * Method stub that invokes native method {@code Ftell}.
     * @param fd RpmFD
     * @return long
     */
    @Override
    public long Ftell(RpmFD fd) {
        try {
            return (long)mh_Ftell.invokeExact(
                    downConvertObject(fd)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Ftell", _t);
        }
    }

    private final MethodHandle mh_Ftell = makeMethodHandle(LONG, "Ftell", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverE}.
     * @param rv RpmEVR
     * @return String
     */
    @Override
    public String rpmverE(RpmEVR rv) {
        try {
            return upConvertString((MemorySegment)mh_rpmverE.invokeExact(
                    downConvertObject(rv)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverE", _t);
        }
    }

    private final MethodHandle mh_rpmverE = makeMethodHandle(STR, "rpmverE", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverEVal}.
     * @param rv RpmEVR
     * @return long
     */
    @Override
    public long rpmverEVal(RpmEVR rv) {
        try {
            return (long)mh_rpmverEVal.invokeExact(
                    downConvertObject(rv)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverEVal", _t);
        }
    }

    private final MethodHandle mh_rpmverEVal = makeMethodHandle(LONG, "rpmverEVal", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverFree}.
     * @param rv RpmEVR
     */
    @Override
    public void rpmverFree(RpmEVR rv) {
        try {
            mh_rpmverFree.invokeExact(
                    downConvertObject(rv)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverFree", _t);
        }
    }

    private final MethodHandle mh_rpmverFree = makeMethodHandle(VOID, "rpmverFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverParse}.
     * @param evr String
     * @return RpmEVR
     */
    @Override
    public RpmEVR rpmverParse(String evr) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(RpmEVR::new, (MemorySegment)mh_rpmverParse.invokeExact(
                    downConvertString(evr, arena)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverParse", _t);
        }
    }

    private final MethodHandle mh_rpmverParse = makeMethodHandle(OBJ, "rpmverParse", STR);

    /**
     * Method stub that invokes native method {@code rpmverR}.
     * @param rv RpmEVR
     * @return String
     */
    @Override
    public String rpmverR(RpmEVR rv) {
        try {
            return upConvertString((MemorySegment)mh_rpmverR.invokeExact(
                    downConvertObject(rv)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverR", _t);
        }
    }

    private final MethodHandle mh_rpmverR = makeMethodHandle(STR, "rpmverR", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverV}.
     * @param rv RpmEVR
     * @return String
     */
    @Override
    public String rpmverV(RpmEVR rv) {
        try {
            return upConvertString((MemorySegment)mh_rpmverV.invokeExact(
                    downConvertObject(rv)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverV", _t);
        }
    }

    private final MethodHandle mh_rpmverV = makeMethodHandle(STR, "rpmverV", OBJ);

    public static Iterable<FunctionDescriptor> getFunctionLayouts() {
        return Arrays.asList(
                FunctionDescriptor.of(INT, OBJ),
                FunctionDescriptor.of(LONG, OBJ),
                FunctionDescriptor.of(OBJ, STR),
                FunctionDescriptor.of(OBJ, STR, STR),
                FunctionDescriptor.of(STR, OBJ),
                FunctionDescriptor.ofVoid(OBJ)
        );
    }
}
