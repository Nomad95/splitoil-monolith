--
SET search_path TO public;

--data:
insert into travel (id, aggregate_id, lobby_id, route, travel_participants)
values (1, '799c470e-b0d6-4df9-8bbe-195533515826', '148091b1-c0f0-4a3e-9b0d-569e05cfcd0f', '{
  "waypoints": [
    {
      "id": "1435ba24-fd5d-4d9a-b19f-6224f0b2e210",
      "location": {
        "lon": 10.0,
        "lat": 15.0
      },
      "waypointType": "BEGINNING_PLACE",
      "historical": false
    },
    {
      "id": "e4a6bf07-b429-464d-8e1c-554aac5e543e",
      "location": {
        "lon": 10.0,
        "lat": 15.0
      },
      "waypointType": "STOP_PLACE",
      "historical": false
    },
    {
      "id": "4db9b61e-6ae8-472f-93ee-6a90e5cb6dae",
      "location": {
        "lon": 10.0,
        "lat": 15.0
      },
      "waypointType": "CHECKPOINT",
      "historical": false
    },
    {
      "id": "1b6a488f-237a-4bad-9f84-c2200aa6f9cf",
      "location": {
        "lon": 10.0,
        "lat": 15.0
      },
      "waypointType": "DESTINATION_PLACE",
      "historical": false
    }
  ]
}', '{
  "participants": [
    {
      "participantId": "31c7b8b8-17fe-46b2-9ec6-df4c1d23f3a4",
      "assignedCarId": "b9574b12-8ca1-4779-aab8-a25192e33739"
    },
    {
      "participantId": "729b78c4-570c-44e7-9f3c-d385c63633d6",
      "assignedCarId": "1ad11d82-5f49-4761-abc3-f8cd9b9bd594"
    },
    {
      "participantId": "a6020a9e-1666-4db4-ae89-03040eb39c42",
      "assignedCarId": "b9574b12-8ca1-4779-aab8-a25192e33739"
    }
  ]
}')