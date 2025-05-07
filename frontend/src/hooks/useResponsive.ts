import { useTheme, Breakpoint } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';

type QueryType = 'up' | 'down' | 'between' | 'only';

export default function useResponsive(
  query: QueryType,
  key: Breakpoint,
  start?: Breakpoint,
  end?: Breakpoint
): boolean {
  const theme = useTheme();

  const mediaUp = useMediaQuery(theme.breakpoints.up(key));

  const mediaDown = useMediaQuery(theme.breakpoints.down(key));

  const mediaBetween = useMediaQuery(
    start && end ? theme.breakpoints.between(start, end) : ''
  );

  const mediaOnly = useMediaQuery(theme.breakpoints.only(key));

  if (query === 'up') {
    return mediaUp;
  }

  if (query === 'down') {
    return mediaDown;
  }

  if (query === 'between') {
    return mediaBetween;
  }

  if (query === 'only') {
    return mediaOnly;
  }

  return false;
}

export function useIsMobile(): boolean {
  return useResponsive('down', 'sm');
}

export function useIsTablet(): boolean {
  return useResponsive('between', 'sm', 'sm', 'md');
}

export function useIsDesktop(): boolean {
  return useResponsive('up', 'md');
} 