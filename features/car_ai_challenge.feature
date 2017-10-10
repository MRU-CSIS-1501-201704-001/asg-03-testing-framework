Feature: Problem Set 04, Car AI Challenge

    Scenario Outline: Desired output from input without errors
        Given a number of adult passengers <numAdultPassengers>
        And a number of child passengers <numChildPassengers>
        And a number of adult pedestrians <numAdultPedestrians>
        And a number of child pedestrians <numChildPedestrians>
        And if asked whether passenger's lives are more important than pedestrian's, I say <choosePassOverPed>
        When I run the Car AI Challenge
        Then I should see a fatality report stating that <fatalityGroup> was killed
        And the number of adult fatalities were <numAdultFatalities>
        And the number of child fatalities were <numChildFatalities>

    Examples:
    | numAdultPassengers | numChildPassengers | numAdultPedestrians | numChildPedestrians | choosePassOverPed | fatalityGroup        | numAdultFatalities | numChildFatalities |
    | 1                  | 0                  | 1                   | 0                   | "y"               | "PEDESTRIANS"        | 1                  | 0                  |
    | 1                  | 0                  | 1                   | 0                   | "Y"               | "PEDESTRIANS"        | 1                  | 0                  |
    | 1                  | 0                  | 1                   | 0                   | "n"               | "OCCUPANTS"         | 1                  | 0                  |
    | 1                  | 0                  | 1                   | 0                   | "N"               | "OCCUPANTS"         | 1                  | 0                  |
    | 0                  | 1                  | 0                   | 1                   | "Y"               | "PEDESTRIANS"        | 0                  | 1                  |
    | 0                  | 1                  | 0                   | 1                   | "N"               | "OCCUPANTS"         | 0                  | 1                  |
    | 1                  | 1                  | 1                   | 1                   | "Y"               | "PEDESTRIANS"        | 1                  | 1                  |
    | 1                  | 1                  | 1                   | 1                   | "n"               | "OCCUPANTS"         | 1                  | 1                  |
    | 5                  | 1                  | 2                   | 3                   | "N"               | "OCCUPANTS"         | 5                  | 1                  |
    | 14                 | 13                 | 15                  | 12                  | "n"               | "PEDESTRIANS"        | 15                 | 12                 |
    | 15                 | 12                 | 14                  | 13                  | "n"               | "OCCUPANTS"         | 15                 | 12                 |