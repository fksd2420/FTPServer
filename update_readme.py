import datetime

def generate_dynamic_content():
    # Replace this logic with your own data fetching or processing
    current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S UTC")
    return f"### 🕒 Last Updated\nThis README was automatically updated on: **{current_time}**"

def update_readme():
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

    # Generate new content
    new_dynamic_content = f"\n{generate_dynamic_content()}\n"

    # Assemble the final README content
    updated_content = before_section + new_dynamic_content + after_section

    with open("README.md", "w", encoding="utf-8") as file:
        file.write(updated_content)

if __name__ == "__main__":
    update_readme()
