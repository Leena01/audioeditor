package org.ql.audioeditor.logic.dbaccess

import org.easymock.EasyMock
import org.ql.audioeditor.common.properties.SongPropertiesLoader
import org.ql.audioeditor.database.DatabaseDao
import org.ql.audioeditor.database.Persistence
import org.ql.audioeditor.database.entities.Song

class DatabaseAccessModelTest extends GroovyTestCase {
    private static final String SONG_PROPERTIES_FILE = "/config/song.properties"
    private static final String FOLDER = "./sound"
    private static final String TITLE_1 = "amajor.wav"
    private static final String TITLE_2 = "aminor.wav"
    private static final String TITLE_3 = "dminor.wav"
    private static String path
    private static File f1
    private static File f2
    private static File f3
    private DatabaseDao database
    private Persistence persistence
    private DatabaseAccessModel databaseAccessModel
    private SongModel songModel1
    private SongModel songModel2
    private SongModel songModel3
    private SongListModel songListModel1
    private SongListModel songListModel2

    void setUp() {
        super.setUp()

        try {
            SongPropertiesLoader.init(SONG_PROPERTIES_FILE)
        } catch (IOException ignored) {
            System.err.println("Error")
        }

        path = this.getClass().getClassLoader().getResource(FOLDER).getPath()
        f1 = new File(path + "/" + TITLE_1)
        f2 = new File(path + "/" + TITLE_2)
        f3 = new File(path + "/" + TITLE_3)
        List<File> files = new ArrayList<>()
        files.add(f1)
        files.add(f2)
        songListModel1 = new SongListModel(files)
        files.add(f3)
        songListModel2 = new SongListModel(files)
        songModel1 = new SongModel(f1)
        songModel2 = new SongModel(f2)
        songModel3 = new SongModel(f3)

        database = EasyMock.createMock(DatabaseDao.class)
        persistence = new Persistence(database)
        databaseAccessModel = new DatabaseAccessModel(persistence)
    }

    void tearDown() {
        database = null
        databaseAccessModel = null
        songModel1 = null
        songModel2 = null
        songModel3 = null
        songListModel1 = null
        songListModel2 = null
    }

    void testGetSongListValid() {
        List<Song> songs = mockSongsValid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(2, act.getItems().size())
        assertEquals(songListModel1.getItems().size(), act.getItems().size())
        assertEquals(songListModel1, act)
    }

    void testGetSongListInvalid() {
        List<Song> songs = mockSongsInvalid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.deleteSong(mockSong3())).andReturn(true)
        EasyMock.replay(database)
        assertEquals(3, database.getSongs().size())
        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(2, act.getItems().size())
        assertEquals(songListModel1.getItems().size(), act.getItems().size())
        assertNotSame(songListModel2.getItems().size(), act.getItems().size())
        assertEquals(songListModel1, act)
        assertNotSame(songListModel2, act)
    }

    void testGetSongList1() {
        List<Song> songs = mockSongsValid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        SongListModel act = databaseAccessModel.getSongList("Album", songModel1)
        assertEquals(2, act.getItems().size())
        assertEquals(songListModel1.getItems().size(), act.getItems().size())
        assertEquals(songListModel1, act)
    }

    void testIsSongValid() {
        List<Song> songs = mockSongsValid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs).times(3)
        EasyMock.replay(database)
        assertTrue(databaseAccessModel.isSongValid(songModel1))
        assertTrue(databaseAccessModel.isSongValid(songModel2))
        assertFalse(databaseAccessModel.isSongValid(songModel3))
    }

    void testAddSong() {
        Song song1 = mockSong1()
        List<Song> songs = new ArrayList<>()
        List<Song> songsExpected = new ArrayList<>()
        songsExpected.add(song1)

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.addSong(song1)).andReturn(true)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected).times(2)
        EasyMock.replay(database)

        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(0, act.getItems().size())

        databaseAccessModel.addSong(songModel1)
        act = databaseAccessModel.getSongList()
        assertEquals(1, act.getItems().size())
        assertEquals(song1, act.getItems().get(0))
    }

    void testAddSongNoSongLoaded() {
        List<Song> songs = new ArrayList<>()

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.addSong(mockSong1())).andReturn(true)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        songModel1.setId(-1)
        String message = shouldFail {
            databaseAccessModel.addSong(songModel1)
        }
        assert message == "No song is loaded."
    }

    void testAddSongAlreadyExists() {
        List<Song> songs = mockSongsValidWithId()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.addSong(mockSong1())).andReturn(true)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        String message = shouldFail {
            databaseAccessModel.addSong(songModel1)
        }
        assert message == "There is already a song with this path."
    }

    void testAddSongNotValid() {
        List<Song> songs = mockSongsValidWithId()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.addSong(mockSong1())).andReturn(true)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        String message = shouldFail {
            databaseAccessModel.addSong(songModel3)
        }
        assert message == "The path specified does not represent a valid file."
    }

    void testAddSongs() {
        Song song1 = mockSong1()
        Song song2 = mockSong2()
        List<Song> songs = new ArrayList<>()
        List<Song> songsExpected = new ArrayList<>()
        songsExpected.add(song1)

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.addSong(song1)).andReturn(true)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)

        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.addSong(song2)).andReturn(true)
        songsExpected.add(song2)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.replay(database)

        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(0, act.getItems().size())

        databaseAccessModel.addSongs(songListModel1)
        act = databaseAccessModel.getSongList()
        assertEquals(2, act.getItems().size())
        assertEquals(songListModel1, act)
    }

    void testAddSongsFail() {
        Song song1 = mockSong1()
        Song song3 = mockSong3()
        List<Song> songs = new ArrayList<>()
        List<Song> songsExpected = new ArrayList<>()
        songsExpected.add(song1)

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.addSong(song1)).andReturn(true)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)

        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.addSong(song3)).andReturn(true)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.replay(database)

        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(0, act.getItems().size())

        String message = shouldFail {
            databaseAccessModel.addSongs(songListModel1)
        }
        assert message == "One of the songs has the following error: The path specified does not represent a valid file."
    }

    void testDeleteSong() {
        Song song1 = mockSong1()
        song1.setId(10)
        List<Song> songs = mockSongsValidWithId()
        List<Song> songsExpected = mockSongsValidWithId()
        songsExpected.remove(song1)

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.deleteSong(song1)).andReturn(true)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.replay(database)

        songModel1.setId(10)
        databaseAccessModel.deleteSong(songModel1)
        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(songsExpected.size(), act.getItems().size())
        assertEquals(songsExpected, act.getItems())
    }

    void testDeleteSongNoSongLoaded() {
        List<Song> songs = mockSongsValidWithId()

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        songModel1.setId(-1)
        String message = shouldFail {
            databaseAccessModel.deleteSong(songModel1)
        }
        assert message == "No song is loaded."
    }

    void testDeleteSongNoSuchSong() {
        List<Song> songs = mockSongsValidWithId()

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        songModel1.setId(24)
        String message = shouldFail {
            databaseAccessModel.deleteSong(songModel1)
        }
        assert message == "There is no such song in the database."
    }

    void testDeleteSongs() {
        List<Song> songs = mockSongsValidWithId()
        Song song1 = songs.get(0)
        Song song2 = songs.get(1)
        List<Song> songsExpected = mockSongsValidWithId()
        songsExpected.remove(0)

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs).times(2)
        EasyMock.expect(persistence.deleteSong(song1)).andReturn(true)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)

        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected).times(2)
        EasyMock.expect(persistence.deleteSong(song2)).andReturn(true)
        songsExpected.remove(0)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.replay(database)

        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(2, act.getItems().size())

        databaseAccessModel.deleteSongs(act)
        act = databaseAccessModel.getSongList()
        assertEquals(0, act.getItems().size())
        assertEquals(new SongListModel(), act)
    }

    void testEditSongNoSongLoaded() {
        List<Song> songs = mockSongsValidWithId()

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        songModel1.setId(-1)
        String message = shouldFail {
            databaseAccessModel.editSong(songModel1)
        }
        assert message == "No song is loaded."
    }

    void testEditSongNoSuchSong() {
        List<Song> songs = mockSongsValidWithId()

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        songModel1.setId(24)
        String message = shouldFail {
            databaseAccessModel.editSong(songModel1)
        }
        assert message == "There is no such song in the database."
    }

    void testEditSong() {
        List<Song> songs = mockSongsValidWithId()
        Song song1 = mockSong1()
        song1.setId(10)
        song1.setAlbum("Album")
        List<Song> songsExpected = mockSongsValidWithId()
        songsExpected.get(0).setAlbum("Album")

        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.editSong(song1)).andReturn(true)
        EasyMock.expect(database.getSongs()).andReturn(songsExpected)
        EasyMock.expect(persistence.getSongs()).andReturn(songsExpected)
        EasyMock.replay(database)

        SongListModel act = databaseAccessModel.getSongList()
        assertEquals(2, act.getItems().size())

        databaseAccessModel.editSong(new SongModel(song1))
        act = databaseAccessModel.getSongList()
        assertEquals(2, act.getItems().size())
        assertEquals("Album", act.getItems().get(0).getAlbum())
    }

    void testGetId() {
        List<Song> songs = mockSongsValid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs).times(4)
        EasyMock.replay(database)
        assertEquals(0, databaseAccessModel.getId(songModel1))
        assertEquals(0, databaseAccessModel.getId(songModel2))
        assertEquals(0, databaseAccessModel.getId(songModel3))

        songModel1.setId(3)
        assertEquals(0, databaseAccessModel.getId(songModel1))
    }

    void testGetId2() {
        List<Song> songs = mockSongsValidWithId()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.getSongs()).andReturn(songs).times(3)
        EasyMock.replay(database)
        assertEquals(10, databaseAccessModel.getId(songModel1))
        assertEquals(3, databaseAccessModel.getId(songModel2))
        assertEquals(0, databaseAccessModel.getId(songModel3))
    }

    void testHasInvalidFalse() {
        List<Song> songs = mockSongsValid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.replay(database)
        assertFalse(databaseAccessModel.hasInvalid())
    }
    void testHasInvalidTrue() {
        List<Song> songs = mockSongsInvalid()
        EasyMock.expect(database.getSongs()).andReturn(songs)
        EasyMock.expect(persistence.deleteSong(mockSong3())).andReturn(true)
        EasyMock.replay(database)
        databaseAccessModel.getSongList()
        assertTrue(databaseAccessModel.hasInvalid())
    }

    private static Song mockSong1() {
        return new Song(TITLE_1,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f1.getAbsolutePath())
    }

    private static Song mockSong2() {
        return new Song(TITLE_2,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f2.getAbsolutePath())
    }

    private static Song mockSong3() {
        return new Song(TITLE_3,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f3.getAbsolutePath())
    }

    private static List<Song> mockSongsValid() {
        List<Song> songs = new ArrayList<>()
        songs.add(new Song(TITLE_1,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f1.getAbsolutePath()))
        songs.add(new Song(TITLE_2,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f2.getAbsolutePath()))
        return songs
    }

    private static List<Song> mockSongsValidWithId() {
        List<Song> songs = new ArrayList<>()
        songs.add(
                new Song(10, TITLE_1,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f1.getAbsolutePath()))
        songs.add(new Song(3, TITLE_2,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f2.getAbsolutePath()))
        return songs
    }

    private static List<Song> mockSongsInvalid() {
        List<Song> songs = new ArrayList<>()
        songs.add(new Song(TITLE_1,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f1.getAbsolutePath()))
        songs.add(new Song(TITLE_2,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f2.getAbsolutePath()))
        songs.add(new Song(TITLE_3,
                SongPropertiesLoader.getDefaultTrack(),
                SongPropertiesLoader.getDefaultArtist(),
                SongPropertiesLoader.getDefaultAlbum(),
                SongPropertiesLoader.getDefaultYear(),
                SongPropertiesLoader.getDefaultGenre(),
                SongPropertiesLoader.getDefaultComment(),
                f3.getAbsolutePath()))
        return songs
    }
}
