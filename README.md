Here are the Ansible task examples for the commonly used commands/modules across various categories:

### 1. **File and Directory Management:**

```yaml
- name: Copy a file from the control node to the managed host
  ansible.builtin.copy:
    src: /path/to/local/file
    dest: /path/to/remote/file

- name: Ensure a directory exists
  ansible.builtin.file:
    path: /path/to/directory
    state: directory
    mode: '0755'

- name: Check if a file exists
  ansible.builtin.stat:
    path: /path/to/file
  register: file_stat

- name: Fetch files from remote machine
  ansible.builtin.fetch:
    src: /path/on/remote/machine/file
    dest: /path/on/control/node/

- name: Replace a line in a file
  ansible.builtin.replace:
    path: /etc/config.cfg
    regexp: 'option=old_value'
    replace: 'option=new_value'

- name: Transfer a Jinja2 template to a remote host
  ansible.builtin.template:
    src: /path/to/template.j2
    dest: /path/to/remote/config.conf

- name: Unarchive a .tar.gz file on the remote host
  ansible.builtin.unarchive:
    src: /path/to/file.tar.gz
    dest: /path/to/destination
```

### 2. **Package Management:**

```yaml
- name: Install a package using APT
  ansible.builtin.apt:
    name: nginx
    state: present

- name: Install a package using YUM
  ansible.builtin.yum:
    name: httpd
    state: present

- name: Install a package using PIP
  ansible.builtin.pip:
    name: requests
    state: present

- name: Install a package using Homebrew on macOS
  ansible.builtin.brew:
    name: wget
    state: present
```

### 3. **Service Management:**

```yaml
- name: Ensure the Apache service is running
  ansible.builtin.service:
    name: apache2
    state: started
    enabled: true

- name: Use systemd to manage a service
  ansible.builtin.systemd:
    name: nginx
    state: restarted

- name: Restart a service using Supervisor
  ansible.builtin.supervisorctl:
    name: my_app
    state: restarted
```

### 4. **User and Group Management:**

```yaml
- name: Create a user
  ansible.builtin.user:
    name: john
    password: "{{ 'password' | password_hash('sha512') }}"
    state: present

- name: Add a user to a group
  ansible.builtin.user:
    name: john
    groups: sudo
    append: yes

- name: Add SSH key to authorized keys
  ansible.builtin.authorized_key:
    user: john
    key: "{{ lookup('file', '/path/to/id_rsa.pub') }}"
    state: present
```

### 5. **Networking:**

```yaml
- name: Download a file using a URL
  ansible.builtin.get_url:
    url: https://example.com/file.tar.gz
    dest: /path/to/file.tar.gz

- name: Manage firewall rules using firewalld
  ansible.builtin.firewalld:
    service: http
    permanent: true
    state: enabled

- name: Wait for port 80 to become available
  ansible.builtin.wait_for:
    port: 80
    state: started

- name: Check HTTP endpoint
  ansible.builtin.uri:
    url: "http://example.com"
    return_content: yes
  register: webpage
```

### 6. **System Information and Control:**

```yaml
- name: Gather facts about the remote host
  ansible.builtin.setup:

- name: Run a command on the remote host
  ansible.builtin.command:
    cmd: ls /var/log

- name: Execute a shell command
  ansible.builtin.shell: |
    echo "This is a shell script"
    touch /tmp/testfile

- name: Print a debug message
  ansible.builtin.debug:
    msg: "The current status is: {{ status }}"
```

### 7. **Database Management:**

```yaml
- name: Create a MySQL database
  ansible.builtin.mysql_db:
    name: my_database
    state: present

- name: Create a PostgreSQL database user
  ansible.builtin.postgresql_user:
    name: dbuser
    password: "secretpassword"
    state: present
```

### 8. **Cloud and Virtualization:**

```yaml
- name: Launch an EC2 instance
  ansible.builtin.ec2:
    key_name: my-key
    instance_type: t2.micro
    image: ami-0abcdef1234567890
    region: us-west-1

- name: Start a Docker container
  ansible.builtin.docker_container:
    name: my_nginx
    image: nginx
    state: started
```

### 9. **Version Control:**

```yaml
- name: Clone a Git repository
  ansible.builtin.git:
    repo: 'https://github.com/my/repo.git'
    dest: /path/to/destination
```

### 10. **Notification and Communication:**

```yaml
- name: Send a Slack notification
  ansible.builtin.slack:
    token: "xoxb-1234567890123-1234567890123"
    channel: "#devops"
    msg: "Deployment completed"
```

### 11. **Miscellaneous:**

```yaml
- name: Include a file of tasks
  ansible.builtin.include_tasks:
    file: tasks/tomcat.yml

- name: Set a fact
  ansible.builtin.set_fact:
    my_var: "Hello World"

- name: Fail if a condition is not met
  ansible.builtin.fail:
    msg: "This task will fail because the condition was not met"
```

### 12. **Container Management:**

```yaml
- name: Build a Docker image
  ansible.builtin.docker_image:
    path: /path/to/Dockerfile
    name: myapp
    state: present

- name: Start a Podman container
  ansible.builtin.podman_container:
    name: my_app
    image: nginx
    state: started
```

### 13. **Handlers:**

```yaml
- name: Restart a service using a handler
  ansible.builtin.notify: restart apache2

handlers:
  - name: restart apache2
    ansible.builtin.service:
      name: apache2
      state: restarted
```

### 14. **Job Control:**

```yaml
- name: Add a cron job
  ansible.builtin.cron:
    name: "daily backup"
    job: "/usr/local/bin/backup.sh"
    hour: 1
    minute: 0
```

### 15. **Windows Specific:**

```yaml
- name: Ensure a Windows service is running
  ansible.windows.win_service:
    name: Spooler
    start_mode: auto
    state: started

- name: Install IIS feature on Windows
  ansible.windows.win_feature:
    name: Web-Server
    state: present
```

This is a broad set of tasks across various categories in Ansible. Each task demonstrates how to use the respective module.
