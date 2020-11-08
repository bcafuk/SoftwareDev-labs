package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDatabaseTest {
    @Test
    public void testConstructor() {
        assertDoesNotThrow(() -> new StudentDatabase(TEST_ROWS));
    }

    @Test
    public void testConstructorThrowsForNullRow() {
        assertThrows(NullPointerException.class, () -> new StudentDatabase(new String[]{null}));
        assertThrows(NullPointerException.class, () -> new StudentDatabase(new String[]{
                "0000000001\tLast\tFirst\t4",
                null,
        }));
    }

    @Test
    public void testConstructorThrowsForWrongNumberOfFields() {
        // 5 fields
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tM.\tFirst\t4"}));

        // 3 fields
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst"}));

        // Trailing, leading or multiple delimiter
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"\t0000000001\tLast\tFirst\t4"}));
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst\t4\t"}));
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\t\tFirst\t4"}));
    }

    @Test
    public void testConstructorThrowsForInvalidNumber() {
        assertThrows(NumberFormatException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst\t"}));
        assertThrows(NumberFormatException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst\tFour"}));

        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst\t0"}));
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst\t6"}));
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{"0000000001\tLast\tFirst\t-1"}));
    }

    @Test
    public void testConstructorThrowsForDuplicateJMBAG() {
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{
                "0000000001\tLast\tFirst\t4",
                "0000000001\tAnother\tOne\t3",
        }));

        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(new String[]{
                "0000000001\tLast\tFirst\t4",
                "0000000004\tDifferent\tName\t2",
                "0000000003\tOne\tMore\t5",
                "0000000001\tAnother\tOne\t3",
                "0000000002\tMore\tYet\t5",
        }));
    }

    @Test
    public void testForJMBAG() {
        StudentDatabase db = new StudentDatabase(TEST_ROWS);

        StudentRecord record = db.forJMBAG("0000000056");
        assertNotNull(record);
        assertEquals("0000000056", record.getJmbag());
        assertEquals("Šimunović", record.getLastName());
        assertEquals("Veljko", record.getFirstName());
        assertEquals(5, record.getFinalGrade());
    }

    @Test
    public void testForJMBAGWithNonexistentJMBAG() {
        StudentDatabase db = new StudentDatabase(TEST_ROWS);

        StudentRecord record = db.forJMBAG("0000000064");
        assertNull(record);
    }

    @Test
    public void testFilterAll() {
        StudentDatabase db = new StudentDatabase(TEST_ROWS);
        List<StudentRecord> records = db.filter(r -> true);

        assertEquals(TEST_ROWS.length, records.size());
    }

    @Test
    public void testFilterNone() {
        StudentDatabase db = new StudentDatabase(TEST_ROWS);
        List<StudentRecord> records = db.filter(r -> false);

        assertEquals(0, records.size());
    }

    @Test
    public void testFilterThrowsForNull() {
        StudentDatabase db = new StudentDatabase(TEST_ROWS);
        assertThrows(NullPointerException.class, () -> db.filter(null));
    }

    private static final String[] TEST_ROWS = new String[]{
            "0000000001\tAkšamović\tMarin\t2",
            "0000000002\tBakamović\tPetra\t3",
            "0000000003\tBosnić\tAndrea\t4",
            "0000000004\tBožić\tMarin\t5",
            "0000000005\tBrezović\tJusufadis\t2",
            "0000000006\tCvrlje\tIvan\t3",
            "0000000007\tČima\tSanjin\t4",
            "0000000008\tĆurić\tMarko\t5",
            "0000000009\tDean\tNataša\t2",
            "0000000010\tDokleja\tLuka\t3",
            "0000000011\tDvorničić\tJura\t4",
            "0000000012\tFranković\tHrvoje\t5",
            "0000000013\tGagić\tMateja\t2",
            "0000000014\tGašić\tMirta\t3",
            "0000000015\tGlavinić Pecotić\tKristijan\t4",
            "0000000016\tGlumac\tMilan\t5",
            "0000000017\tGrđan\tGoran\t2",
            "0000000018\tGužvinec\tMatija\t3",
            "0000000019\tGvardijan\tSlaven\t4",
            "0000000020\tHibner\tSonja\t5",
            "0000000021\tJakobušić\tAntonija\t2",
            "0000000022\tJurina\tFilip\t3",
            "0000000023\tKalvarešin\tAna\t4",
            "0000000024\tKarlović\tĐive\t5",
            "0000000025\tKatanić\tDino\t2",
            "0000000026\tKatunarić\tZoran\t3",
            "0000000027\tKomunjer\tLuka\t4",
            "0000000028\tKosanović\tNenad\t5",
            "0000000029\tKos-Grabar\tIvo\t2",
            "0000000030\tKovačević\tBoris\t3",
            "0000000031\tKrušelj Posavec\tBojan\t4",
            "0000000032\tLučev\tTomislav\t5",
            "0000000033\tMachiedo\tIvor\t2",
            "0000000034\tMajić\tDiana\t3",
            "0000000035\tMarić\tIvan\t4",
            "0000000036\tMarkić\tAnte\t5",
            "0000000037\tMarkoč\tDomagoj\t2",
            "0000000038\tMarkotić\tJosip\t3",
            "0000000039\tMartinec\tJelena\t4",
            "0000000040\tMišura\tZrinka\t5",
            "0000000041\tOrešković\tJakša\t2",
            "0000000042\tPalajić\tNikola\t3",
            "0000000043\tPerica\tKrešimir\t4",
            "0000000044\tPilat\tIvan\t5",
            "0000000045\tRahle\tVedran\t2",
            "0000000046\tRajtar\tRobert\t3",
            "0000000047\tRakipović\tIvan\t4",
            "0000000048\tRezić\tBruno\t5",
            "0000000049\tSaratlija\tBranimir\t2",
            "0000000050\tSikirica\tAlen\t3",
            "0000000051\tSkočir\tAndro\t4",
            "0000000052\tSlijepčević\tJosip\t5",
            "0000000053\tSrdarević\tDario\t2",
            "0000000054\tŠamija\tPavle\t3",
            "0000000055\tŠimunov\tIvan\t4",
            "0000000056\tŠimunović\tVeljko\t5",
            "0000000057\tŠiranović\tHrvoje\t2",
            "0000000058\tŠoić\tVice\t3",
            "0000000059\tŠtruml\tMarin\t4",
            "0000000060\tVignjević\tIrena\t5",
            "0000000061\tVukojević\tRenato\t2",
            "0000000062\tZadro\tKristijan\t3",
            "0000000063\tŽabčić\tŽeljko\t4",
    };
}
