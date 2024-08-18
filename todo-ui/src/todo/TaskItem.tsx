import React, {useState} from 'react';
import { Checkbox, ListItem, ListItemText, IconButton, Box, Chip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import {TaskDto, UpdateTaskDto} from "../generated/models";
import {deleteTask, toggleTaskStatus, updateTask} from "../generated/endpoints/task-controller/task-controller.ts";
import EditTaskModal from "./EditTaskModal.tsx";

interface TaskItemProps {
    task: TaskDto;
    onTaskUpdated: () => void;
    onTaskDeleted: () => void;
}

const TaskItem: React.FC<TaskItemProps> = ({ task, onTaskUpdated, onTaskDeleted }) => {
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);

    const handleToggleDone = async () => {
        const response = await toggleTaskStatus(task.id); // Используем новый метод toggleTaskStatus
        if (response.status === 200) {
            onTaskUpdated(); // Обновляем список задач
        }
    };

    const handleDelete = async () => {
        const response = await deleteTask(task.id);
        if (response.status === 200) {  // Если задача успешно удалена
            onTaskDeleted(); // Обновляем список задач
        }
    };

    const handleEditTask = async (updatedTask: UpdateTaskDto) => {
        const response = await updateTask(task.id, updatedTask);
        if (response.status === 200) {
            onTaskUpdated(); // Обновляем список задач после сохранения
        }
    };

    return (
        <>
            <ListItem
                secondaryAction={
                    <>
                        <IconButton edge="end" aria-label="edit" onClick={() => setIsEditModalOpen(true)}>
                            <EditIcon />
                        </IconButton>
                        <IconButton edge="end" aria-label="delete" onClick={handleDelete}>
                            <DeleteIcon />
                        </IconButton>
                    </>
                }
            >
                <Checkbox checked={task.done} onChange={handleToggleDone} />
                <ListItemText
                    primary={task.title}
                    secondary={
                        <>
                            {task.description}
                            <Box sx={{ mt: 1 }}>
                                {task.tags.map((tag) => (
                                    <Chip key={tag} label={tag} sx={{ mr: 0.5 }} />
                                ))}
                            </Box>
                        </>
                    }
                />
            </ListItem>
            <EditTaskModal
                task={task}
                isOpen={isEditModalOpen}
                onClose={() => setIsEditModalOpen(false)}
                onSave={handleEditTask}
            />
        </>
    );
};

export default TaskItem;
