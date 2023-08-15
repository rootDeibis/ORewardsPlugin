![ORewards Plugin](https://i.imgur.com/QAypmK7.gif)

Reward system for minecraft servers.


### Features:
#### Rewards for time, one-time, permissions, and more..
- HexColor Support
- Category system
- MYSQL and SQL FILE Support
- Setting up rewards for separate files
- Multiple rewards
- Customizable menus
- PlaceholderAPI implementation
- Using textures on menu heads
#### Default placeholders defined by the resource:
- <player_name> - Applicable for all
- <reward_displayname> - Applicable for reward files
- <reward_cooldown> - Applicable for reward files
- <reward_permission> - Applicable for reward files
- <rewards_availables_names> - Applicable for welcome message
- <rewards_availables> - Applicable for welcome message
- <available_rewards> - Applicable for category gui
#### Placeholders by PlaceholderAPI:
- %orewards_available_[name]% - Returns if the reward is available, the result of which is editable in the config.yml file.
- %orewards_availables% - Return how many rewards you have available
- %orewards_cooldown_[name]% - Returns the countdown format
#### Commands:
- /orewards - orewards.command
- /orewards menu - orewards.cmd.menu
- /orewards open <category_name> (optional: <player_name>) - orewards.cmd.open
- /orewards help - orewards.cmd.help
- /orewards reset <player_name> <reward_name|all> - orewards.cmd.reset
- /orewards reload - orewards.cmd.reload
