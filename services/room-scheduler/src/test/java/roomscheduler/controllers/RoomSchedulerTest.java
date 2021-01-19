package roomscheduler.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.NameDateInfo;
import roomscheduler.entities.SlotInfo;

@SpringBootTest
class RoomSchedulerTest {

    public transient List<IdNamePair> listNamePair;
    public transient List<SlotInfo> listDateIntPairs;
    public transient List<Integer> listslots;

    @BeforeEach
    void setup() {
        listNamePair = new ArrayList<>();
        listDateIntPairs = new ArrayList<>();
        listslots = new ArrayList<>();
    }

    @Test
    void testFindSuitableDate() {
        IdNamePair np = new IdNamePair(0, "test5");
        listNamePair.add(np);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        listslots.add(2);

        NameDateInfo expected = new NameDateInfo(date, "test5", 0);
        List<NameDateInfo> expectedList = new ArrayList<>();
        expectedList.add(expected);
        assertEquals(RoomScheduler.findSuitable(listNamePair,
                listDateIntPairs, listslots), expectedList);
    }

    @Test
    void testFindSuitableDateOverlappingyears() {
        IdNamePair np = new IdNamePair(0, "test1");
        listNamePair.add(np);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        listslots.add(0);

        List<NameDateInfo> expectedList = new ArrayList<>();
        assertEquals(RoomScheduler.findSuitable(listNamePair,
                listDateIntPairs, listslots), expectedList);
    }

    @Test
    void testFindSuitableDateNoSuitableRooms() {
        IdNamePair np = new IdNamePair(1, "test2");
        listNamePair.add(np);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        listslots.add(2);

        List<NameDateInfo> expectedList = new ArrayList<>();
        assertEquals(RoomScheduler.findSuitable(listNamePair,
                listDateIntPairs, listslots), expectedList);
    }

    @Test
    void testFindSuitableDateSecondRoom() {
        IdNamePair np = new IdNamePair(2, "test3");
        listNamePair.add(np);
        IdNamePair np2 = new IdNamePair(1, "test4");
        listNamePair.add(np2);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        Timestamp date2 = new Timestamp(121, 10, 1, 12, 30, 0, 0);
        SlotInfo si2 = new SlotInfo(date2, 1, 1);
        listDateIntPairs.add(si);
        listDateIntPairs.add(si2);
        listslots.add(2);

        NameDateInfo expected = new NameDateInfo(date2, "test4", 1);
        List<NameDateInfo> expectedList = new ArrayList<>();
        expectedList.add(expected);
        assertEquals(RoomScheduler.findSuitable(listNamePair,
                listDateIntPairs, listslots), expectedList);
    }

    @Test
    void testFindSuitableDateFirstRoom() {
        IdNamePair np = new IdNamePair(0, "test56");
        listNamePair.add(np);
        IdNamePair np2 = new IdNamePair(1, "test4");
        listNamePair.add(np2);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        Timestamp date2 = new Timestamp(121, 10, 1, 12, 30, 0, 0);
        SlotInfo si2 = new SlotInfo(date2, 1, 1);
        listDateIntPairs.add(si);
        listDateIntPairs.add(si2);
        listslots.add(2);

        NameDateInfo expected = new NameDateInfo(date, "test6", 0);
        List<NameDateInfo> expectedList = new ArrayList<>();
        expectedList.add(expected);
        assertEquals(RoomScheduler.findSuitable(listNamePair,
                listDateIntPairs, listslots), expectedList);
    }
}