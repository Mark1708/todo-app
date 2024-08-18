import { List } from '@mui/material';
import TaskItem from './TaskItem';
import {TaskDto} from "../generated/models";

interface TaskListProps {
    tasks: TaskDto[];
    onTaskUpdated: () => void;
    onTaskDeleted: (id: string) => void;
}

const TaskList: React.FC<TaskListProps> = ({ tasks, onTaskUpdated, onTaskDeleted }) => {
    return (
        <List>
            {tasks.map((task) => (
                <TaskItem
                    key={task.id}
                    task={task}
                    onTaskUpdated={onTaskUpdated}
                    onTaskDeleted={() => onTaskDeleted(task.id)}
                />
            ))}
        </List>
    );
};

export default TaskList;
