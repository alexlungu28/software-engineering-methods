package roomscheduler.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.NameDateInfo;
import roomscheduler.entities.RoomSchedule;
import roomscheduler.entities.SlotInfo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void TestFindSuitableDate() {
        IdNamePair np = new IdNamePair(0, "test5");
        listNamePair.add(np);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        listslots.add(2);

        NameDateInfo expected = new NameDateInfo(date, "test5", 0);
        List<NameDateInfo> expectedList = new ArrayList<>();
        expectedList.add(expected);
        assertEquals(RoomScheduler.findSuitable(listNamePair, listDateIntPairs, listslots), expectedList);
    }

    @Test
    void TestFindSuitableDateOverlappingyears() {
        IdNamePair np = new IdNamePair(0, "test5");
        listNamePair.add(np);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        listslots.add(0);

        List<NameDateInfo> expectedList = new ArrayList<>();
        assertEquals(RoomScheduler.findSuitable(listNamePair, listDateIntPairs, listslots), expectedList);
    }

    @Test
    void TestFindSuitableDateNoSuitableRooms() {
        IdNamePair np = new IdNamePair(1, "test5");
        listNamePair.add(np);
        Timestamp date = new Timestamp(121, 9, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(date, 0, 0);
        listDateIntPairs.add(si);
        listslots.add(2);

        List<NameDateInfo> expectedList = new ArrayList<>();
        assertEquals(RoomScheduler.findSuitable(listNamePair, listDateIntPairs, listslots), expectedList);
    }

    @Test
    void TestFindSuitableDateSecondRoom() {
        IdNamePair np = new IdNamePair(2, "test5");
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
        assertEquals(RoomScheduler.findSuitable(listNamePair, listDateIntPairs, listslots), expectedList);
    }

    @Test
    void TestFindSuitableDateFirstRoom() {
        IdNamePair np = new IdNamePair(0, "test5");
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

        NameDateInfo expected = new NameDateInfo(date, "test5", 0);
        List<NameDateInfo> expectedList = new ArrayList<>();
        expectedList.add(expected);
        assertEquals(RoomScheduler.findSuitable(listNamePair, listDateIntPairs, listslots), expectedList);
    }


}
//    public SlotInfo(Timestamp date, Integer roomId, Integer roomSlotId) {
//        this.date = date;
//        this.roomId = roomId;
//        this.roomSlotId = roomSlotId;
//    }
//
//    public static List<NameDateInfo> findSuitable(List<IdNamePair> roomInfoWithRequiredCapacity,
//                                                  List<SlotInfo> dateIntPairs,
//                                                  List<Integer> slotIdsToNotOverlapSameYear) {
//        List<NameDateInfo> finalResult = new ArrayList<>();
//        outer:
//        for (IdNamePair i : roomInfoWithRequiredCapacity) {
//            for (SlotInfo pair : dateIntPairs) {
//                if (i.getId() == pair.getRoomId() && !slotIdsToNotOverlapSameYear
//                        .contains(pair.roomSlotId)) {
//                    finalResult.add(new NameDateInfo(pair.getDate(),
//                            i.getName(), pair.getRoomSlotId()));
//                    //once we found one room slot to schedule the lecture, we can
//                    //stop searching
//                    break outer;
//                }
//            }
//        }
//        return finalResult;
//    }