package roomscheduler.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import roomscheduler.communication.RoomScheduleCommunication;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.NameDateInfo;
import roomscheduler.entities.RoomSchedule;
import roomscheduler.entities.SlotInfo;
import roomscheduler.repositories.RoomScheduleRepository;


public class RoomScheduler {


    /**
     * Method that finds an appropriate room slot for the lecture to be scheduled.
     *
     * @param roomInfoWithRequiredCapacity rooms with at least the required capacity
     * @param dateIntPairs available room slots for the input given
     * @param slotIdsToNotOverlapSameYear room slots ids for lectures of the same year
     *                                    of study that would cause overlap
     * @return empty list of list with one appropriate room slot
     */
    public static List<NameDateInfo> findSuitable(List<IdNamePair> roomInfoWithRequiredCapacity,
                                                  List<SlotInfo> dateIntPairs,
                                                  List<Integer> slotIdsToNotOverlapSameYear) {
        List<NameDateInfo> finalResult = new ArrayList<>();
        outer:
        for (IdNamePair i : roomInfoWithRequiredCapacity) {
            for (SlotInfo pair : dateIntPairs) {
                if (i.getId() == pair.getRoomId() && !slotIdsToNotOverlapSameYear
                        .contains(pair.roomSlotId)) {
                    finalResult.add(new NameDateInfo(pair.getDate(),
                            i.getName(), pair.getRoomSlotId()));
                    //once we found one room slot to schedule the lecture, we can
                    //stop searching
                    break outer;
                }
            }
        }
        return finalResult;
    }

    /**
     * Method for saving the scheduled lecture in the database and marking the
     * room slots as occupied.
     *
     * @param roomScheduleRepository roomScheduleRepository
     * @param finalResult filteredList
     * @param lectureId lectureId
     * @param yearOfStudy yearOfStudy
     * @param numSlots numSlots
     * @return message
     * @throws IOException exception
     */
    public static String scheduleLecture(RoomScheduleRepository roomScheduleRepository,
                                         List<NameDateInfo> finalResult, Integer lectureId,
                                         Integer yearOfStudy, Integer numSlots) throws IOException {
        NameDateInfo result = finalResult.get(0);
        roomScheduleRepository.saveAndFlush(new RoomSchedule(
                result.getRoomSlotId(), lectureId, yearOfStudy));
        for (int i = 0; i < numSlots; i++) {
            roomScheduleRepository.saveAndFlush(new
                    RoomSchedule(result.getRoomSlotId()
                    + (i + 1) * 20, lectureId, yearOfStudy));
        }
        RoomScheduleCommunication.makeRoomSlotsOccupied(result.getRoomSlotId(), numSlots);
        return "Your lecture was successfully scheduled for: " +
                result.getDate().toString() +
                " in the room with name: " + result.getRoomName();
    }
}
