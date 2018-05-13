package org.ql.audioeditor.logic.dbaccess

import org.easymock.EasyMock
import org.ql.audioeditor.database.Persistence

class DatabaseAccessModelTest extends GroovyTestCase {
    void setUp() {
        super.setUp()
        SongModel songModel1 = new SongModel(new File("../../../sound/amajor.mp3"))
        SongModel songModel2 = new SongModel(new File("../../../sound/aminor.mp3"))
        Persistence persistence = EasyMock.createMock(Persistence.class)
    }

    void tearDown() {
        songModel1 = null
        songModel2 = null
        persistence = null
    }

    void testGetSongList() {
    }

    void testGetSongList1() {
    }

    void testIsSongValid() {
    }

    void testAddSong() {
    }

    void testAddSongs() {
    }

    void testDeleteSong() {
    }

    void testDeleteSongs() {
    }

    void testEditSong() {
    }

    void testGetId() {
    }

    void testHasInvalid() {
    }
}
