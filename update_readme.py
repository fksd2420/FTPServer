import datetime
import requests
from pathlib import Path


response = requests.get("https://api.github.com/repos/fksd2420/FTPServer/releases")
message = ""
if response.status_code == 200:

    json = response.json()

    for item : json:
        for asset : item["assets"]:
            if asset["name"].endswith(".apk"):
                with open(f"downloads/{item.tag_name}.txt", "w") as file:
                    file.write(asset["download_count"])

    total_count = 0
    dir_path = Path("downloads")
    for item in dir_path.iterdir():
    if item.is_file():
        total_count += int(item.read_text())
    message = "Download count: " + str(total_count)
else:
    message = "Download count: NA"
    
current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S UTC+5")
last_update =  f"### 🕒 Last Updated\nThis README was automatically updated on: {current_time}"

with open("README.md", "r", encoding="utf-8") as file:
    readme_content = file.read()

# Define the markers
start_marker = "<!-- START_SECTION:update_zone -->"
end_marker = "<!-- END_SECTION:update_zone -->"

# Find the positions of the markers
start_idx = readme_content.find(start_marker)
end_idx = readme_content.find(end_marker)

if start_idx == -1 or end_idx == -1:
    print("Error: Could not find section markers in README.md")
    return

# Extract sections before and after the dynamic area
before_section = readme_content[:start_idx + len(start_marker)]
after_section = readme_content[end_idx:]

new_dynamic_content = message + "\n" + last_update

updated_content = before_section + new_dynamic_content + after_section

with open("README.md", "w", encoding="utf-8") as file:
    file.write(updated_content)
