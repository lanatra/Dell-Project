package DataLayer;

import Domain.Poe;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Lasse on 22-04-2015.
 */
public class PoeMapperTest {

    PoeMapper mapper;
    int proj_id;
    String filename;
    int user_id;
    String filetype;


    @Before
    public void setUp() throws Exception {
        mapper = new PoeMapper();

    }

    @org.junit.Test
    public void testAddPoeFile() throws Exception {
        System.out.println("DUMMY VALUES:");
        proj_id = 10;
        filename = "eyyy.txt";
        user_id = 2;
        filetype = "txt";


        mapper.addPoeFile(proj_id, filename, user_id, filetype, DatabaseConnection.getInstance().getConnection());

        ArrayList<Poe> poes= mapper.getPoe(proj_id, DatabaseConnection.getInstance().getConnection());

        int checkForMatch = -1;

        for (int i = 0; i < poes.size(); i++) {
            if (filename.equals(poes.get(i).getFilename())) {
                checkForMatch = i;
            }
        }
        if (checkForMatch != -1) {
            assertTrue(poes.get(checkForMatch).getFilename().equals(filename));
        } else {
            fail();
        }

    }


    @After
    public void resetDbChanges() throws Exception {

        mapper.deletePoe(filename, proj_id, DatabaseConnection.getInstance().getConnection());
    }

    @org.junit.Test
    public void testDeletePoe() throws Exception {

    }

    @org.junit.Test
    public void testGetPoe() throws Exception {



    }

    @org.junit.Test
    public void testGetNextPoEId() throws Exception {

    }
}